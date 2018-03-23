package update.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import common.Common;
import listener.UpdateListener;
import model.FirmWareInformation;
import util.MyLocalDevice;
import update.view.UpdateView;
import util.Draper;
import util.Public;
import util.STExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/1/20.
 */
public class UpdatePresenterImpl implements UpdatePresenter {

    private final List<RemoteDevice> mRemoteDevices;
    private final Map<Integer, RemoteDevice> mRemoteDeviceMap;
    private UpdateListener updateListener;
    private UpdateView mUpdateView;
    private File framefile;
    private File framefile1;
    private File framefile2;
    private byte[] fileTmp;
    private byte[] fileTmp1;
    private byte[] fileTmp2;
    private FirmWareInformation firmWareInformation;
    private FirmWareInformation firmWareInformation1 = new FirmWareInformation();
    private FirmWareInformation firmWareInformation2 = new FirmWareInformation();
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


    public UpdatePresenterImpl(UpdateView mUpdateView) {
        this.mUpdateView = mUpdateView;
        localDevice = MyLocalDevice.getInstance();
        mRemoteDevices = localDevice.getRemoteDevices();
        mRemoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        mUpdateView.updateDevBox(mRemoteDevices);
        updateListener = new UpdateListener(localDevice, this, mUpdateView);
    }

    /**
     * 选择固件，并初始化
     */
    @Override
    public void choosebt() {
        JFileChooser fDialog = new JFileChooser();
        BinFileFilter binFileFilter = new BinFileFilter();
        fDialog.setFileFilter(binFileFilter);
        fDialog.setDialogTitle("Please choose Frameware File");
        int returnVal = fDialog.showOpenDialog(null);
        //打印出文件的路径，你可以修改位 把路径值 写到 textField 中
        if (JFileChooser.APPROVE_OPTION == returnVal) {
            try {
                mUpdateView.updateFileText(fDialog.getSelectedFile().getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            framefile1 = fDialog.getSelectedFile();
            ReadFileTobuff(framefile1);
            CheckFileType(framefile1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(framefile1.lastModified());
            String s = sdf.format(cal.getTime());
            mUpdateView.updateLastModify(s);
        }
    }

    @Override
    public void choosebt2() {
        JFileChooser fDialog = new JFileChooser();
        BinFileFilter binFileFilter = new BinFileFilter();
        fDialog.setFileFilter(binFileFilter);
        fDialog.setDialogTitle("Please choose Frameware File");
        int returnVal = fDialog.showOpenDialog(null);
        //打印出文件的路径，你可以修改位 把路径值 写到 textField 中
        if (JFileChooser.APPROVE_OPTION == returnVal) {
            try {
                mUpdateView.updateFileText2(fDialog.getSelectedFile().getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            framefile2 = fDialog.getSelectedFile();
            CheckFileType2(framefile2);
            ReadFileTobuff2(framefile2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(framefile2.lastModified());
//            String s = sdf.format(cal.getTime());
//            mUpdateView.updateLastModify(s);
        }
    }

    /**
     * 检测文件类型
     *
     * @param file
     * @return
     */
    public boolean CheckFileType(File file) {
        try {
            FileInputStream fi = new FileInputStream(file);
            // fi.skip(0);
            byte[] tmp = new byte[8];
            fi.read(tmp, 0, 8);
//            int startAddress=(tmp[6]&0xffffff)*256+tmp[5]&0xffffff;
            int startAddress = (int) (tmp[6] & 0xff) * 256 + (int) (tmp[5] & 0xff);

            if(Public.matchString(firmWareInformation1.getType(),"MC-AC-EX-")){
                if (startAddress < 0x4100) {
                    firmWareInformation1.setTypeNum(0);
                } else if (startAddress < 0x4400) {
                    firmWareInformation1.setTypeNum(1);
                } else {
                    firmWareInformation1.setTypeNum(2);
                }
            }else {
                if (startAddress < 0x0080) {
                    firmWareInformation1.setTypeNum(0);
                } else if (startAddress < 0x0240) {
                    firmWareInformation1.setTypeNum(1);
                } else {
                    firmWareInformation1.setTypeNum(2);
                }
            }
            mUpdateView.updateVersionAndType(firmWareInformation1);
            if (tmp[1] == 122 && tmp[3] == 0 && tmp[4] == 32) {
                return true;
            }
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测文件类型
     *
     * @param file
     * @return
     */
    public boolean CheckFileType2(File file) {
        try {
            FileInputStream fi = new FileInputStream(file);
            // fi.skip(0);
            byte[] tmp = new byte[8];
            fi.read(tmp, 0, 8);
//            int startAddress=(tmp[6]&0xffffff)*256+tmp[5]&0xffffff;
            int startAddress = (int) (tmp[6] & 0xff) * 256 + (int) (tmp[5] & 0xff);

            if (startAddress < 0x0080) {
                firmWareInformation2.setTypeNum(0);
                //mUpdateView.updateTypeLabelText("0");
            } else if (startAddress < 0x0240) {
                firmWareInformation2.setTypeNum(1);
                //mUpdateView.updateTypeLabelText("1");
            } else {
                firmWareInformation2.setTypeNum(2);
                //mUpdateView.updateTypeLabelText("2");
            }
            if (tmp[1] == 122 && tmp[3] == 0 && tmp[4] == 32) {
                return true;
            }
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取固件信息
     */
    private void ReadFileTobuff(File file) {
        try {
            fileTmp1 = new byte[(int) file.length()];
            FileInputStream fi = new FileInputStream(file);
            int offset = 0, numRead = 0;
            while (offset < fileTmp1.length
                    && (numRead = fi.read(fileTmp1, offset, fileTmp1.length - offset)) >= 0) {
                offset += numRead;
            }
            String s = new String(fileTmp1);
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
                firmWareInformation1.setType(type);
                firmWareInformation1.setMajorNum(Integer.valueOf(split[0]));
                firmWareInformation1.setMinorNum(Integer.valueOf(split[1]));
                firmWareInformation1.setPatchNum(Integer.valueOf(split[2]));
                mUpdateView.updateVersionAndType(firmWareInformation1);
            }
            fi.close();
        } catch (Exception e1) {
            mUpdateView.showError("Firm corrupt!");
            e1.printStackTrace();
        }
    }

    /**
     * 读取固件信息
     */
    private void ReadFileTobuff2(File file) {
        try {
            fileTmp2 = new byte[(int) file.length()];
            FileInputStream fi = new FileInputStream(file);
            int offset = 0, numRead = 0;
            while (offset < fileTmp2.length
                    && (numRead = fi.read(fileTmp2, offset, fileTmp2.length - offset)) >= 0) {
                offset += numRead;
            }
            String s = new String(fileTmp2);
            boolean right = false;
            String name = Public.matchStr(s, "--\\{DARPER\\sFIREWARE\\}--");
            String type = Public.matchStr(s, "--\\[[A-Za-z0-9-/]*\\]--");
            type = type.substring(3, type.length() - 3);
            String version = Public.matchStr(s, "--\\*v[0-9]+.[0-9]+.[0-9]+\\*--");
            version = version.substring(4, version.length() - 3);
            String[] split = version.split("\\.");
            if ("--{DARPER FIREWARE}--".equals(name) && !"".equals(type) && !"".equals(version)) {
                right = true;
            }
            if (right) {
                firmWareInformation2.setType(type);
                firmWareInformation2.setMajorNum(Integer.valueOf(split[0]));
                firmWareInformation2.setMinorNum(Integer.valueOf(split[1]));
                firmWareInformation2.setPatchNum(Integer.valueOf(split[2]));
                mUpdateView.updateVersionAndType(firmWareInformation2);
            }
            fi.close();
        } catch (Exception e1) {
            mUpdateView.showError("Firm corrupt!");
            e1.printStackTrace();
        }
    }

    @Override
    public boolean getUpdateState() {
        return isSendCompleted;
    }


    @Override
    public byte[] getfileTmp() {
        return fileTmp;
    }

    @Override
    public String getFirmWareType() {
        return firmWareInformation.getType();
    }

    @Override
    public synchronized void addJListDevice(RemoteDevice device) {
        if (flag == 1) {
            addJListDeviceOrigin(device);
        } else if (flag == 2) {
            addJListDeviceBefore(device);
        } else if (flag == 3) {
            addJListDeviceAfter(device);
        }
    }

    @Override
    public void addJListDeviceOrigin(RemoteDevice device) {
        if (isSingle && device.equals(mUpdateView.getdevBoxSelectedItem())) {
            String version = Public.readVersion(device);
            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber());
            mUpdateView.showOriginalDeviceVersion(device.getInstanceNumber() + "--" + version);
            findOriginDevice(Common.DEVICE_FOUND_ALL);
        } else {
            String version = Public.readVersion(device);
            count++;
            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + MyLocalDevice.getAddressList().size());
            mUpdateView.showOriginalDeviceVersion(device.getInstanceNumber() + "--" + version);
        }
    }

    @Override
    public void addJListDeviceBefore(RemoteDevice device) {
        String version = Public.readVersion(device);
        int typeNum = firmWareInformation.getTypeNum();
        switch (typeNum) {
            case 1:
                if (Public.matchString(version, "(A)")) {
                    versionType = Common.DEVICE_VERSION_DEFFER;
                    mAbnormalRemoteDevice.add(device);
                }
                break;
            case 2:
                if (Public.matchString(version, "(B)")) {
                    versionType = Common.DEVICE_VERSION_DEFFER;
                    mAbnormalRemoteDevice.add(device);
                }
                break;
        }
        if (isSingle && device.equals(mUpdateView.getdevBoxSelectedItem())) {
            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber() + "    1/1");
            mUpdateView.showBeforeDeviceVersion(device.getInstanceNumber() + "--" + version);
        } else {
            count++;
            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + mUpdateView.getOriginalSize());
            mUpdateView.showBeforeDeviceVersion(device.getInstanceNumber() + "--" + version);
        }
    }

    @Override
    public void addJListDeviceAfter(RemoteDevice device) {
        if (isSingle) {
            String version = Public.readVersion(device);
            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber() + "    1/1");
            mUpdateView.showAfterDeviceVersion(device.getInstanceNumber() + "--" + version);
        } else {
            count++;
            String version = Public.readVersion(device);
            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + mUpdateView.getOriginalSize());
            mUpdateView.showAfterDeviceVersion(device.getInstanceNumber() + "--" + version);
        }
    }

    @Override
    public void update(boolean isCancel) {
        synchronized (lockConfirm) {
            this.isCancel = isCancel;
            if (isCancel) {
                flag = 1;
                count = 0;
                isSendCompleted = true;
            } else {
                flag = 3;
                count = 0;
            }
            lockConfirm.notify();
        }
    }

    @Override
    public void cancelListener() {
        localDevice.getEventHandler().removeListener(updateListener);
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public void updateToSelectButton() {
        framefile = framefile1;
        fileTmp = fileTmp1;
        firmWareInformation = firmWareInformation1;
        updateOne();
    }

    private void initUpdate() {
        flag = 1;
        count = 0;
        isCancel=false;
        mAbnormalRemoteDevice.clear();
        versionType = Common.DEVICE_VERSION_SAME;
        originDeviceFlag = Common.DEVICE_FOUNDING;
        updateListener.clearRemoteDeviceList();
        localDevice.getEventHandler().addListener(updateListener);
//        mUpdateView.showUpgradeInformation("add listener");
    }

    private void updateOne() {
        try {
            isSingle = true;
            initUpdate();
            Send send = new Send(this, updateListener);
            send.send(new WhoIsRequest());
            mUpdateView.showUpgradeInformation(Common.STEP_1_START);
            mUpdateView.showUpgradeInformation("search the device for upgrade");
            synchronized (lockOrigin) {
                lockOrigin.wait();
            }
            mUpdateView.showUpgradeInformation(Common.STEP_1_END);
            if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
                mUpdateView.showUpgradeInformation("exit！");
                return;
            }
            if (framefile == null) {
                framefile = new File(mUpdateView.getFileName());
            }
            //重置进度条
            percent = 0;
            flag = 2;
            count = 0;
            isSendCompleted = false;
            mUpdateView.updateProgress(0);
            mUpdateView.showUpgradeInformation(Common.STEP_2_START);
            new Thread(new UpdatePresenterImpl.RunUpdateToOne(framefile, localDevice, (mUpdateView.getdevBoxSelectedItem()))).start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class RunUpdateToOne implements Runnable {
        LocalDevice dev;
        File updateFile;
        RemoteDevice peer;

        RunUpdateToOne(File file, LocalDevice dev, RemoteDevice peer) {
            updateFile = file;
            this.dev = dev;
            this.peer = peer;
        }

        @Override
        public void run() {
            try {
                Draper.sendIHaveFrameToOne(firmWareInformation.getType(), peer, firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), firmWareInformation.getTypeNum(), updateFile);
                mUpdateView.showUpgradeInformation("send the ready command");
                //确保升级前找到所有设备，否则不升级,如果升级前找到了所以设备，则升级，否则进入等待
                Thread.sleep(6000);
                updateListener.clearRemoteDeviceList();
                Send send = new Send(UpdatePresenterImpl.this, updateListener);
                send.send(new WhoIsRequest());
                mUpdateView.showUpgradeInformation("search for the prepared device");
//                if (mUpdateView.getOriginalSize() != mUpdateView.getBeforeSize()) {
                synchronized (lockBefore) {
                    lockBefore.wait();
                }
//                }
                Thread.sleep(1000);
                mUpdateView.showUpgradeInformation(Common.STEP_2_END);
                if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
                    mUpdateView.showUpgradeInformation("exit！");
                    return;
                }
                if (versionType == Common.DEVICE_VERSION_DEFFER) {
                    //弹出确认升级对话框
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (versionType == Common.DEVICE_VERSION_DEFFER) {
                                mUpdateView.showConfirmDialog("Version exception");
                            } else {
                                mUpdateView.showConfirmDialog("Version normal");
                            }
                        }
                    };
                    STExecutor.submit(runnable);

                    mUpdateView.showUpgradeInformation("prepare to upgrade");
                    synchronized (lockConfirm) {
                        lockConfirm.wait();
                    }
                }
                Thread.sleep(1000);
                flag = 3;
                count = 0;
                updateListener.clearRemoteDeviceList();
                if (isCancel) {
                    mUpdateView.showUpgradeInformation("upgrade cancel");
                    return;
                }
                if(mUpdateView.getBeforeSize()==getAbnormalRemoteDeviceSize()){
                    mUpdateView.showConfirmDialog("All firmware is the latest ,update finish!");
                    isSendCompleted = true;
                    mUpdateView.updateFinish();
                    return;
                }
                mUpdateView.showUpgradeInformation(Common.STEP_3_START);
                mUpdateView.showUpgradeInformation("begin to upgrade");
                FileInputStream fi = new FileInputStream(updateFile);
                byte[] buffer = new byte[(int) updateFile.length()];
                int offset = 0;
                int numRead = 0;
                while (offset < buffer.length
                        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                    offset += numRead;
                }
                // 确保所有数据均被读取
                if (offset != buffer.length) {
                    throw new IOException("Could not  read file completely"
                            + framefile.getName());
                } else {
                    int sent = 0;
                    while (sent < buffer.length) {
                        sent = sentBuffer(buffer, sent);
                    }
                }
                fi.close();

                //更新进度条的UI
                List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
                mTotalSize = frameToSend.size();
                new UpdatePercent().run();

            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {

            }
        }

        private synchronized int sentBuffer(byte[] buffer, int sent) throws BACnetException, InterruptedException {
            if (buffer.length - sent > 256) {
                byte[] data = new byte[256];
                System.arraycopy(buffer, sent, data, 0, 256);
                if (localDevice != null)
                    Draper.sendFrameWareToOne(firmWareInformation.getType(), peer, firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                if (localDevice != null)
                    Draper.sendFrameWareToOne(firmWareInformation.getType(), peer, firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), buffer.length, sent, data1);
                sent = buffer.length;
            }
            int delay = mUpdateView.getDelay();
            if (delay != 0) {
                Thread.sleep(delay);
            }
//            percent = sent * 100 / buffer.length;
            return sent;
        }
    }


    @Override
    public void updateButton() {
        framefile = framefile1;
        fileTmp = fileTmp1;
        firmWareInformation = firmWareInformation1;
        updateAll();
    }

    /**
     * 升级所有电机
     */
    private void updateAll() {
        try {
            isSingle = false;
            initUpdate();
            Send send = new Send(this, updateListener);
            send.send(new WhoIsRequest());
            mUpdateView.showUpgradeInformation(Common.STEP_1_START);
            mUpdateView.showUpgradeInformation("search the device for upgrade");
            synchronized (lockOrigin) {
                lockOrigin.wait();
            }
            mUpdateView.showUpgradeInformation(Common.STEP_1_END);
            if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
                mUpdateView.showUpgradeInformation("exit！");
                return;
            }
            if (framefile == null) {
                framefile = new File(mUpdateView.getFileName());
            }
            //重置进度条
            percent = 0;
            flag = 2;
            count = 0;
            isSendCompleted = false;
            mUpdateView.updateProgress(0);
            mUpdateView.showUpgradeInformation(Common.STEP_2_START);
            new Thread(new UpdatePresenterImpl.RunUpdate(framefile, localDevice)).start();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public class RunUpdate implements Runnable {
        LocalDevice dev;
        File updateFile;

        RunUpdate(File file, LocalDevice dev) {
            updateFile = file;
            this.dev = dev;
        }

        @Override
        public void run() {
            try {
                //电机装备阶段 若a-> a 电机会重启，a->b不会重启
                Draper.sendIHaveFrame(firmWareInformation.getType(), firmWareInformation.getMajorNum(), firmWareInformation.getMinorNum(), firmWareInformation.getPatchNum(), firmWareInformation.getTypeNum(), updateFile);
                mUpdateView.showUpgradeInformation("send the ready command");
                Thread.sleep(6000);
                updateListener.clearRemoteDeviceList();
                Send send = new Send(UpdatePresenterImpl.this, updateListener);
                send.send(new WhoIsRequest());
                mUpdateView.showUpgradeInformation("search for the prepared device");
                synchronized (lockBefore) {
                    lockBefore.wait();
                }
                Thread.sleep(1000);
                mUpdateView.showUpgradeInformation(Common.STEP_2_END);
                if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
                    mUpdateView.showUpgradeInformation("exit！");
                    return;
                }
                //版本异常弹出确认对话框，正常直接升级
                if (versionType == Common.DEVICE_VERSION_DEFFER) {
                    //弹出确认升级对话框
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            if (versionType == Common.DEVICE_VERSION_DEFFER) {
                                mUpdateView.showConfirmDialog("Version exception");
                            } else {
                                mUpdateView.showConfirmDialog("Version normal");
                            }
                        }
                    };
                    STExecutor.submit(runnable);

                    mUpdateView.showUpgradeInformation("prepare to upgrade");
                    synchronized (lockConfirm) {
                        lockConfirm.wait();
                    }
                }
                Thread.sleep(1000);
                flag = 3;
                count = 0;
                updateListener.clearRemoteDeviceList();
                if (isCancel) {
                    mUpdateView.showUpgradeInformation("upgrade cancel");
                    return;
                }
                if(mUpdateView.getBeforeSize()==getAbnormalRemoteDeviceSize()){
                    mUpdateView.showConfirmDialog("All firmware is the latest ,update finish!");
                    isSendCompleted = true;
                    mUpdateView.updateFinish();
                    return;
                }
                mUpdateView.showUpgradeInformation(Common.STEP_3_START);
                mUpdateView.showUpgradeInformation("begin to upgrade");
                FileInputStream fi = new FileInputStream(updateFile);
                byte[] buffer = new byte[(int) updateFile.length()];
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
                    int sent = 0;
                    setBuffer(buffer, sent);
                }
                fi.close();

                //更新进度条的UI
                List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
                mTotalSize = frameToSend.size();
                new UpdatePercent().run();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {

            }
        }

        private void setBuffer(byte[] buffer, int sent) throws BACnetException, InterruptedException {
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
                int delay = mUpdateView.getDelay();
                if (delay != 0) {
                    Thread.sleep(delay);
                }
//                        percent = sent * 100 / offset;
            }
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
                        mUpdateView.showUpgradeInformation("update error ,please update again!");
                        isSendCompleted = true;
                        t.stop();
                    }

                    mUpdateView.updateProgress(percent);
                    Timer t = (Timer) e.getSource();
                    // 如果进度条达到最大值重新开发计数
                    if (percent == 100) {
                        mUpdateView.showUpgradeInformation("send complete，wait for devices restarted");
                        isSendCompleted = true;
                        t.stop();
                    }
                }
            });
            time.start();
        }
    }

    @Override
    public AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address) {
        if (vendorId.intValue() != 900) {
            return null;
        }
        Sequence parms = (Sequence) serviceParameters;
        int blockSize = ((UnsignedInteger) parms.getValues().get(Draper.GET_FRIMEBLOCK_BLOCK_SZIE)).intValue();
        int startOffset = ((UnsignedInteger) parms.getValues().get(Draper.GET_FRIMEBLOCK_START_OFFSET)).intValue();
        switch (serviceNumber.intValue()) {
            case Draper.GETFRAME_CONF_SERSUM:
                SequenceOf<Primitive> list = new SequenceOf<>();
                list.add(new UnsignedInteger(startOffset));
                if (framefile == null) {
                    framefile = new File(mUpdateView.getFileName());
                    ReadFileTobuff(framefile);
                }

                byte[] buffer = new byte[blockSize];
                if (startOffset + blockSize > fileTmp.length) {
                    System.arraycopy(fileTmp, startOffset, buffer, 0, fileTmp.length - startOffset);
                } else {
                    System.arraycopy(fileTmp, startOffset, buffer, 0, blockSize);
                }

                list.add(new OctetString(buffer));
                return new ConfirmedPrivateTransferAck(vendorId, serviceNumber, list);
        }
        return null;
    }

    @Override
    public void findBeforeDevice() {
        originDeviceFlag = Common.DEVICE_FOUND_ALL;
        synchronized (lockBefore) {
            lockBefore.notify();
        }
    }

    @Override
    public void findOriginDevice(int flag) {
        originDeviceFlag = flag;
        synchronized (lockOrigin) {
            lockOrigin.notify();
        }
    }

    @Override
    public void cancelUpgrade() {
        originDeviceFlag = Common.DEVICE_UPDATE_EXIT;
        synchronized (lockConfirm) {
            lockConfirm.notify();
        }
        JOptionPane.showMessageDialog(null, "Time out! Please try again!", "alert", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public int getAbnormalRemoteDeviceSize() {
        return mAbnormalRemoteDevice.size();
    }

    @Override
    public void autoUpdate1() {
        framefile = framefile1;
        fileTmp = fileTmp1;
        firmWareInformation = firmWareInformation1;
        updateAll();
    }

    @Override
    public void autoUpdate2() {
        framefile = framefile2;
        fileTmp = fileTmp2;
        firmWareInformation = firmWareInformation2;
        updateAll();
    }

    @Override
    public void autoOneUpdate1() {
        framefile = framefile1;
        fileTmp = fileTmp1;
        firmWareInformation = firmWareInformation1;
        updateOne();
    }

    @Override
    public void autoOneUpdate2() {
        framefile = framefile2;
        fileTmp = fileTmp2;
        firmWareInformation = firmWareInformation2;
        updateOne();
    }
}
