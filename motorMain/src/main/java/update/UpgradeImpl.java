package update;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import common.Common;
import listener.UpdateListener;
import listener.UpgradeRemoteListener;
import model.FirmWareInformation;
import update.presenter.Send;
import util.Draper;
import util.MyLocalDevice;
import util.Public;
import util.STExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpgradeImpl implements IUpgrade{

    private File framefile;
    private byte[] fileTmp;
    private FirmWareInformation firmWareInformation;
    private LocalDevice localDevice;
    /**
     * 发送状态 true 发送完成 false 发送中
     */
    private boolean isSendCompleted = true;

    /**
     * 升级进度
     */
    private int percent;
    private int mTotalSize;

    Object lockOrigin = new Object();
    Object lockBefore = new Object();
    Object lockConfirm = new Object();

    /**
     * 是否取消升级
     */
    private boolean isCancel;

    /**
     * 升级日志显示 1.代表原始 2.代表升级前 3.代表升级后
     */
    private int flag = 1;

    /**
     * 0 代表没找全电机 1 代表全部找到电机 -1 代表退出升级
     */
    private int originDeviceFlag = Common.DEVICE_FOUNDING;

    /**
     * true 代表升级所有，false 代表升级单个
     */
    public static boolean isSingle = false;

    /**
     * 设备个数
     */
    public int count = 0;

    /**
     * 版本判断，1 代表正常，-1 代表异常  异常：要升级的电机的版本和估计的版本一致
     */
    public int versionType = Common.DEVICE_VERSION_SAME;

    /**
     * 与type不匹配的设备列表
     */
    public List<RemoteDevice> mAbnormalRemoteDevice = new ArrayList<>();

    /**
     * 待升级的设备
     */
    public List<RemoteDevice> mOriginRemoteDevice = new ArrayList<>();

    /**
     * 升级前的设备
     */
    public List<RemoteDevice> mBeforeRemoteDevice = new ArrayList<>();

    /**
     * 升级后的设备
     */
    public List<RemoteDevice> mAfterRemoteDevice = new ArrayList<>();

    private UpgradeRemoteListener upgradeRemoteListener;

    @Override
    public FirmWareInformation chooseFirmware(String path) {
        FirmWareInformation firmWareInformation = new FirmWareInformation();
        File file = new File(path);
        try {
            byte[] fileTmp = new byte[(int) file.length()];
            FileInputStream fi = new FileInputStream(file);
            int offset = 0, numRead = 0;
            while (offset < fileTmp.length
                    && (numRead = fi.read(fileTmp, offset, fileTmp.length - offset)) >= 0) {
                offset += numRead;
            }
            fi.close();
            String s = new String(fileTmp);
            boolean right = false;
            String name = Public.matchStr(s, "--\\{DARPER\\sFIREWARE\\}--");
//            String type = getString(s, "\\[[a-zA-Z]{2}-[a-zA-Z]{2}-[0-9]{2}\\]");
            String type = Public.matchStr(s, "--\\[[A-Za-z0-9-/]*\\]--");
            type = type.substring(3, type.length() - 3);
            String version = Public.matchStr(s, "--\\*v[0-9]+.[0-9]+.[0-9]+\\*--");
            version = version.substring(4, version.length() - 3);
            String[] split = version.split("\\.");
            if ("--{DARPER FIREWARE}--".equals(name) && !"".equals(type) && !"".equals(version)) {
                right = true;
            }

            if (right) {

                firmWareInformation.setType(type);
                firmWareInformation.setMajorNum(Integer.valueOf(split[0]));
                firmWareInformation.setMinorNum(Integer.valueOf(split[1]));
                firmWareInformation.setPatchNum(Integer.valueOf(split[2]));
                byte[] tmp = new byte[8];
                System.arraycopy(fileTmp,0,tmp,0,8);
                int startAddress = (int) (tmp[6] & 0xff) * 256 + (int) (tmp[5] & 0xff);
                if(Public.matchString(type,"MC-AC-EX-")){
                    if (startAddress < 0x4100) {
                        firmWareInformation.setTypeNum(0);
                    } else if (startAddress < 0x4400) {
                        firmWareInformation.setTypeNum(1);
                    } else {
                        firmWareInformation.setTypeNum(2);
                    }
                }else {
                    if (startAddress < 0x0080) {
                        firmWareInformation.setTypeNum(0);
                    } else if (startAddress < 0x0240) {
                        firmWareInformation.setTypeNum(1);
                    } else {
                        firmWareInformation.setTypeNum(2);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            firmWareInformation=null;
            e.printStackTrace();
        } catch (IOException e) {
            firmWareInformation=null;
            e.printStackTrace();
        }
        return firmWareInformation;
    }

    @Override
    public void startUpdate() {
        updateStep1();
        if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
//                mUpdateView.showUpgradeInformation("exit！");
            return;
        }
        updateStep2();
        if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
//                mUpdateView.showUpgradeInformation("exit！");
            return;
        }
        updateStep3();
    }

    private void updateStep1(){
        localDevice = MyLocalDevice.getInstance();
        upgradeRemoteListener = new UpgradeRemoteListener(localDevice,this);
        initUpdate();
        try {
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            synchronized (lockOrigin) {
                lockOrigin.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    private void updateStep2(){
        try {
            if (framefile == null) {

            }
            //重置进度条
            percent = 0;
            flag = 2;
            count = 0;
            isSendCompleted = false;
            //电机装备阶段 若a-> a 电机会重启，a->b不会重启
            Draper.sendIHaveFrame(firmWareInformation.getType(), firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), firmWareInformation.getTypeNum(), framefile);
//            mUpdateView.showUpgradeInformation("send the ready command");
            Thread.sleep(6000);
            upgradeRemoteListener.clearRemoteDeviceList();
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
//            mUpdateView.showUpgradeInformation("search for the prepared device");
            synchronized (lockBefore) {
                lockBefore.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BACnetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStep3(){
        try {
            //版本异常弹出确认对话框，正常直接升级
            if (versionType == Common.DEVICE_VERSION_DEFFER) {
                //弹出确认升级对话框
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        if (versionType == Common.DEVICE_VERSION_DEFFER) {
//                            mUpdateView.showConfirmDialog("Version exception");
                        } else {
//                            mUpdateView.showConfirmDialog("Version normal");
                        }
                    }
                };
                STExecutor.submit(runnable);

//                mUpdateView.showUpgradeInformation("prepare to upgrade");
                synchronized (lockConfirm) {
                    lockConfirm.wait();
                }
            }
            Thread.sleep(1000);
            flag = 3;
            count = 0;
            upgradeRemoteListener.clearRemoteDeviceList();
            if (isCancel) {
//                mUpdateView.showUpgradeInformation("upgrade cancel");
                return;
            }
            if(mBeforeRemoteDevice.size()==mAbnormalRemoteDevice.size()){
//                mUpdateView.showConfirmDialog("All firmware is the latest ,update finish!");
                isSendCompleted = true;
//                mUpdateView.updateFinish();
                return;
            }
//            mUpdateView.showUpgradeInformation(Common.STEP_3_START);
//            mUpdateView.showUpgradeInformation("begin to upgrade");
            FileInputStream fi = new FileInputStream(framefile);
            byte[] buffer = new byte[(int) framefile.length()];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }

            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not read file completely"
                        + framefile.getName());
            } else {
                setBuffer(buffer);
            }
            fi.close();

            //更新进度条的UI
            List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
            mTotalSize = frameToSend.size();
            new UpdatePercent().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BACnetException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBuffer(byte[] buffer) throws BACnetException, InterruptedException {
        int sent = 0;
        while (sent < buffer.length) {
            if (buffer.length - sent > 256) {
                byte[] data = new byte[256];
                System.arraycopy(buffer, sent, data, 0, 256);
                if (localDevice != null)
                    Draper.sendFrameWare(firmWareInformation.getType(), firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                if (localDevice != null)
                    Draper.sendFrameWare(firmWareInformation.getType(), firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), buffer.length, sent, data1);
                sent = buffer.length;
            }
//            int delay = mUpdateView.getDelay();
//            if (delay != 0) {
//                Thread.sleep(delay);
//            }
//                        percent = sent * 100 / offset;
        }
    }

    public class UpdatePercent implements Runnable {

        public UpdatePercent() {
        }

        @Override
        public void run() {
            Timer time = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    percent=percent+5;
                    List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
//                    System.out.println("UpdatePresenterImpl frameToSend.size" + frameToSend.size());
                    try{
                        percent = 100 - frameToSend.size() * 100 / mTotalSize;
                    }catch (Exception e1){
                        Timer t = (Timer) e.getSource();
//                        mUpdateView.showUpgradeInformation("update error ,please update again!");
                        isSendCompleted = true;
                        t.stop();
                    }

//                    mUpdateView.updateProgress(percent);
                    Timer t = (Timer) e.getSource();
                    // 如果进度条达到最大值重新开发计数
                    if (percent == 100) {
//                        mUpdateView.showUpgradeInformation("send complete，wait for devices restarted");
                        isSendCompleted = true;
                        t.stop();
                    }
                }
            });
            time.start();
        }
    }

    private void initUpdate() {
        flag = 1;
        count = 0;
        isCancel=false;
        mAbnormalRemoteDevice.clear();
        versionType = Common.DEVICE_VERSION_SAME;
        originDeviceFlag = Common.DEVICE_FOUNDING;
        upgradeRemoteListener.clearRemoteDeviceList();
        localDevice.getEventHandler().addListener(upgradeRemoteListener);
    }

    @Override
    public int getUpgradeProgress() {
        return 0;
    }

    @Override
    public boolean getUpdateState() {
        return false;
    }

    @Override
    public int getFlag() {
        return 0;
    }

    @Override
    public String getFirmWareType() {
        return null;
    }

    @Override
    public void addJListDevice(RemoteDevice device) {

    }

    @Override
    public void findOriginDevice(int flag) {
        originDeviceFlag = flag;
        synchronized (lockOrigin) {
            lockOrigin.notify();
        }
    }
}
