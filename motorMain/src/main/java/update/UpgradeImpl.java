package update;

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
import listener.UpgradeRemoteListener;
import manager.rmi.IUpgradeCallback;
import model.Conf;
import model.FirmWareInformation;
import model.FirmWareResult;
import util.Draper;
import util.GsonUtils;
import util.MyLocalDevice;
import util.Public;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UpgradeImpl implements IUpgrade {

    private byte[] mFileTmp;
    private byte[] mFileTmp1;
    private byte[] mFileTmp2;
    private FirmWareInformation mFirmWareInformation;
    private FirmWareInformation mFirmWareInformation1;
    private FirmWareInformation mFirmWareInformation2;
    private LocalDevice localDevice;
    /**
     * 是否升级完成
     */
    private boolean isUpgradeCompleted = true;

    /**
     * 升级进度
     */
    private int percent;
    private int mTotalSize;

    Object lockOrigin = new Object();
    Object lockBefore = new Object();
//    Object lockConfirm = new Object();

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
     * 升级模式
     */
    private int upgradeMode;

    /**
     * 升级次数
     */
    private int upgradeCount;

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
    private IUpgradeCallback upgradeCallback;
    private int masterPercent;
    private int slavePercent;

    Conf conf=null;

    public void getUpgradeCallbackShowPercent() {
        showProgress(masterPercent,slavePercent);
    }

    public void getUpgradeCallbackShowPercent(int slaveProgress) {
        int masterProgress=masterPercent;
        slavePercent=slaveProgress;
        showProgress(masterProgress,slaveProgress);
    }

    public void getUpgradeCallbackShowPercent(int masterProgress,int slaveProgress) {
        masterPercent=masterProgress;
        slavePercent=slaveProgress;
        showProgress(masterProgress,slaveProgress);
    }

    private void showProgress(int masterProgress,int slaveProgress){
        if(upgradeCallback!=null){
            try {
                upgradeCallback.onProgressChanged(masterProgress,slaveProgress);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUpgradeCallbackShowLog(String masterInfo,String slaveInfo) {
        if(upgradeCallback!=null){
            try {
                upgradeCallback.showLog(masterInfo,slaveInfo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public FirmWareResult chooseFirmware(String path) {
        FirmWareResult firmWareResult = new FirmWareResult();
        List<FirmWareInformation> firmWareInformationList = new ArrayList<>();
        List<ZipEntry> fileList=new ArrayList<>();
        File file = new File(path);
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipFile zipFile = new ZipFile(path);
            ZipEntry zipEn = null;
            while ((zipEn = zis.getNextEntry()) != null) {
                if (!zipEn.isDirectory()) {

                    if(zipEn.getName().equals("conf.ini")){
                        byte[] tmpZipEn = new byte[(int) zipEn.getSize()];
                        BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEn));
                        bis.read(tmpZipEn);
                        String s = new String(tmpZipEn);
                        conf = GsonUtils.parseJSON(s, Conf.class);
                        bis.close();
                    }else {
                        fileList.add(zipEn);
                    }
                }
            }
            for (ZipEntry zipEntry:fileList){
                if(!zipEntry.isDirectory()){
                    byte[] tmpZipEn = new byte[(int) zipEntry.getSize()];
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    bis.read(tmpZipEn);
                    int crc = Public.crcCalcData(tmpZipEn);
                    if(Public.matchString(zipEntry.getName(),"(#1)")){
                        mFileTmp1 = tmpZipEn;
                        FirmWareInformation firmWareInformation1 = conf.getFirmWareInformation1();
                        if(crc!=firmWareInformation1.getCrc()){
                            firmWareResult.setCode(-1);
                            firmWareResult.setMessage(firmWareResult.getMessage()+" firmware (A) corrupt");
                            continue;
                        }
                        mFirmWareInformation1 = firmWareInformation1;
                        firmWareInformationList.add(conf.getFirmWareInformation1());
                    }else if(Public.matchString(zipEntry.getName(),"(#2)")){
                        mFileTmp2 = tmpZipEn;
                        FirmWareInformation firmWareInformation2 = conf.getFirmWareInformation2();
                        if(crc!=firmWareInformation2.getCrc()){
                            firmWareResult.setCode(-1);
                            firmWareResult.setMessage(firmWareResult.getMessage()+" firmware (B) corrupt");
                            continue;
                        }
                        mFirmWareInformation2 = firmWareInformation2;
                        firmWareInformationList.add(conf.getFirmWareInformation2());
                    }
                    bis.close();
                }
            }
            zis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        firmWareResult.setFirmWareInformationList(firmWareInformationList);
        return firmWareResult;
    }

    @Override
    public List<FirmWareInformation> chooseFirmware(File file) {
        List<FirmWareInformation> firmWareInformationList = new ArrayList<>();
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipFile zipFile = new ZipFile(file);
            ZipEntry zipEn = null;
            while ((zipEn = zis.getNextEntry()) != null) {
                if (!zipEn.isDirectory()) {
                    byte[] tmpZipEn = new byte[(int) zipEn.getSize()];
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEn));
                    bis.read(tmpZipEn);
                    String s = new String(tmpZipEn);
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
                        FirmWareInformation firmWareInformation = new FirmWareInformation();
                        firmWareInformation.setType(type);
                        firmWareInformation.setMajorNum(Integer.valueOf(split[0]));
                        firmWareInformation.setMinorNum(Integer.valueOf(split[1]));
                        firmWareInformation.setPatchNum(Integer.valueOf(split[2]));
                        byte[] tmp = new byte[8];
                        System.arraycopy(tmpZipEn, 0, tmp, 0, 8);
                        int startAddress = (int) (tmp[6] & 0xff) * 256 + (int) (tmp[5] & 0xff);
                        if (Public.matchString(type, "MC-AC-EX-")) {
                            if (startAddress < 0x4100) {
                                firmWareInformation.setTypeNum(0);
                            } else if (startAddress < 0x4400) {
                                firmWareInformation.setTypeNum(1);
                            } else {
                                firmWareInformation.setTypeNum(2);
                            }
                        } else {
                            if (startAddress < 0x0080) {
                                firmWareInformation.setTypeNum(0);
                            } else if (startAddress < 0x0240) {
                                firmWareInformation.setTypeNum(1);
                            } else {
                                firmWareInformation.setTypeNum(2);
                            }
                        }
                        if (firmWareInformation.getTypeNum() == 1) {
                            mFileTmp1 = tmpZipEn;
                            mFirmWareInformation1 = firmWareInformation;
                            firmWareInformationList.add(firmWareInformation);
                        } else if (firmWareInformation.getTypeNum() == 2) {
                            mFileTmp2 = tmpZipEn;
                            mFirmWareInformation2 = firmWareInformation;
                            firmWareInformationList.add(firmWareInformation);
                        }

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firmWareInformationList;
    }

    @Override
    public void startUpdate(IUpgradeCallback callback) {
        this.upgradeCallback=callback;
        if(upgradeCallback!=null){
            try {
                upgradeCallback.onStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        getUpgradeCallbackShowLog("init",null);
        initUpgrade();
        upgradeDevice();
        new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(flag==3){
                    if (mBeforeRemoteDevice.size() == (mAfterRemoteDevice.size() + mAbnormalRemoteDevice.size())) {
                        switch (upgradeMode){
                            case Common.UPGRADE_12:
                                if(++upgradeCount==2){
                                    //升级结束
                                    cancelListener();
                                    if(upgradeCallback!=null){
                                        getUpgradeCallbackShowPercent(100,100);
                                        try {
                                            upgradeCallback.onFinish();
                                        } catch (RemoteException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    Timer t = (Timer) e.getSource();
                                    // 如果进度条达到最大值重新开发计数
                                        isUpgradeCompleted = true;
                                        t.stop();
                                    System.out.println("upgrade finish 2");
                                }else {
                                    getUpgradeCallbackShowPercent(50,100);
                                    changeUpgradeFirmware();
                                    upgradeDevice();
                                }
                                break;
                            case Common.UPGRADE_121:
                                if(++upgradeCount==3){
                                    //升级结束
                                    cancelListener();
                                    if(upgradeCallback!=null){
                                        getUpgradeCallbackShowPercent(100,100);
//                                        upgradeCallback.onFinish();
                                    }
                                    Timer t = (Timer) e.getSource();
                                    // 如果进度条达到最大值重新开发计数
                                        isUpgradeCompleted = true;
                                        t.stop();
                                    System.out.println("upgrade finish 3");
                                }else {
                                    masterPercent+=30;
                                    getUpgradeCallbackShowPercent(100);
                                    changeUpgradeFirmware();
                                    upgradeDevice();
                                }
                                break;
                        }
                    }
                }
            }
        }).start();
    }

    private void upgradeDevice(){
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                System.out.println("step 1 start");
                updateStep1();
                getUpgradeCallbackShowPercent(15);
                System.out.println("step 1 end");
                if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
//                mUpdateView.showUpgradeInformation("exit！");
                    return;
                }
                System.out.println("step 2 start");
                updateStep2();
                getUpgradeCallbackShowPercent(30);
                System.out.println("step 2 end");
                if (originDeviceFlag == Common.DEVICE_UPDATE_EXIT) {
//                mUpdateView.showUpgradeInformation("exit！");
                    return;
                }
                System.out.println("step 3 start");
                updateStep3();
                System.out.println("step 3 end");
            }
        };
        new Thread(runnable).start();
    }

    private void initUpgrade() {
        localDevice = MyLocalDevice.getInstance();
        upgradeRemoteListener = new UpgradeRemoteListener(this);
        localDevice.getEventHandler().addListener(upgradeRemoteListener);
        mFileTmp = mFileTmp1;
        mFirmWareInformation = mFirmWareInformation1;
        isUpgradeCompleted = false;
        upgradeCount=0;
        getUpgradeCallbackShowPercent(0,0);
    }

    private void initData1() {
        flag = 1;
        isCancel = false;
        slavePercent=0;
        getUpgradeCallbackShowPercent();
        originDeviceFlag = Common.DEVICE_FOUNDING;
        mAbnormalRemoteDevice.clear();
        mOriginRemoteDevice.clear();
        mBeforeRemoteDevice.clear();
        mAfterRemoteDevice.clear();
        upgradeRemoteListener.clearRemoteDeviceList();
    }

    private void updateStep1() {
        initData1();
        try {
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            synchronized (lockOrigin) {
                lockOrigin.wait();
            }
            if(remoteDeviceListTypeVersion(mOriginRemoteDevice)==1){
                changeUpgradeFirmware();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return 0: 代表版本混杂 1; 代表全为A； 2：代表全为B
     */
    private int remoteDeviceListTypeVersion(List<RemoteDevice> remoteDeviceList){
        int a=0;
        int b=0;
        for (RemoteDevice remoteDevice:remoteDeviceList){
            String version = Public.readVersion(remoteDevice);
            if(Public.matchString(version, "(A)")){
                a++;
            }else {
                b++;
            }
            if(a!=0 && b!=0){
                return 0;
            }
        }
        return (a==0)?2:1;

    }

    private void initData2() {
        //重置进度条
        percent = 0;
        flag = 2;
        upgradeRemoteListener.clearRemoteDeviceList();
    }

    private void updateStep2() {
        try {
            //电机装备阶段 若a-> a 电机会重启，a->b不会重启
            Draper.sendIHaveFrame(mFirmWareInformation.getType(), mFirmWareInformation.getMajorNum(), mFirmWareInformation.getMinorNum(), mFirmWareInformation.getPatchNum(), mFirmWareInformation.getTypeNum(), mFileTmp);
//            mUpdateView.showUpgradeInformation("send the ready command");
            Thread.sleep(6000);
            initData2();
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

    private void initData3(){
        flag = 3;
        upgradeRemoteListener.clearRemoteDeviceList();
    }

    private void updateStep3() {
        if(remoteDeviceListTypeVersion(mBeforeRemoteDevice)!=0){
            upgradeMode=Common.UPGRADE_12;
        }else {
            upgradeMode=Common.UPGRADE_121;
        }
        try {
            initData3();
            if (isCancel) {
//                mUpdateView.showUpgradeInformation("upgrade cancel");
                return;
            }
//            mUpdateView.showUpgradeInformation(Common.STEP_3_START);
//            mUpdateView.showUpgradeInformation("begin to upgrade");

            setBuffer(mFileTmp);

            //更新进度条的UI
            List<Frame> frameToSend = MyLocalDevice.getFrameToSend();
            mTotalSize = frameToSend.size();
            new UpdatePercent().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BACnetException e) {
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
                    Draper.sendFrameWare(mFirmWareInformation.getType(), mFirmWareInformation.getMajorNum(), mFirmWareInformation.getMinorNum(), mFirmWareInformation.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                if (localDevice != null)
                    Draper.sendFrameWare(mFirmWareInformation.getType(), mFirmWareInformation.getMajorNum(), mFirmWareInformation.getMinorNum(), mFirmWareInformation.getPatchNum(), buffer.length, sent, data1);
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
                    try {
                        percent = 100 - frameToSend.size() * 100 / mTotalSize;
                        if((percent%5==0)){
                            slavePercent=30+percent/2;
                            getUpgradeCallbackShowPercent(slavePercent);
                        }
                    } catch (Exception e1) {
                        Timer t = (Timer) e.getSource();
//                        mUpdateView.showUpgradeInformation("update error ,please update again!");
                        isUpgradeCompleted = true;
                        t.stop();
                    }

//                    mUpdateView.updateProgress(percent);
                    Timer t = (Timer) e.getSource();
                    // 如果进度条达到最大值重新开发计数
                    if (percent == 100) {
//                        mUpdateView.showUpgradeInformation("send complete，wait for devices restarted");
                        isUpgradeCompleted = true;
                        t.stop();
                    }
                }
            });
            time.start();
        }
    }

    @Override
    public int getUpgradeProgress() {
        return percent;
    }

    @Override
    public boolean getUpdateState() {
        return isUpgradeCompleted;
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public String getFirmWareType() {
        return mFirmWareInformation.getType();
    }

    @Override
    public void addJListDevice(RemoteDevice device) {
        if (flag == 1) {
            addJListDeviceOrigin(device);
        } else if (flag == 2) {
            addJListDeviceBefore(device);
        } else if (flag == 3) {
            addJListDeviceAfter(device);
        }
    }

    public void addJListDeviceOrigin(RemoteDevice device) {
//        if (isSingle && device.equals(mUpdateView.getdevBoxSelectedItem())) {
//            String version = Public.readVersion(device);
//            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber());
//            mUpdateView.showOriginalDeviceVersion(device.getInstanceNumber() + "--" + version);
//            findOriginDevice(Common.DEVICE_FOUND_ALL);
//        } else {
        mOriginRemoteDevice.add(device);
//        String version = Public.readVersion(device);
//        Boolean aBoolean = Public.matchString(version, "(A)");
//            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + MyLocalDevice.getAddressList().size());
//            mUpdateView.showOriginalDeviceVersion(device.getInstanceNumber() + "--" + version);
//        }
    }

    public void addJListDeviceBefore(RemoteDevice device) {
        String version = Public.readVersion(device);
        int typeNum = mFirmWareInformation.getTypeNum();
        switch (typeNum) {
            case 1:
                if (Public.matchString(version, "(A)")) {
                    mAbnormalRemoteDevice.add(device);
                }
                break;
            case 2:
                if (Public.matchString(version, "(B)")) {
                    mAbnormalRemoteDevice.add(device);
                }
                break;
        }
        mBeforeRemoteDevice.add(device);
        if (mOriginRemoteDevice.size() == mBeforeRemoteDevice.size()) {
            findBeforeDevice();
        }
//        if (isSingle && device.equals(mUpdateView.getdevBoxSelectedItem())) {
//            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber() + "    1/1");
//            mUpdateView.showBeforeDeviceVersion(device.getInstanceNumber() + "--" + version);
//        } else {
//            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + mUpdateView.getOriginalSize());
//            mUpdateView.showBeforeDeviceVersion(device.getInstanceNumber() + "--" + version);
//        }
    }

    public void addJListDeviceAfter(RemoteDevice device) {
//        if (isSingle) {
//            String version = Public.readVersion(device);
//            mUpdateView.showUpgradeInformation("single found device：" + device.getInstanceNumber() + "    1/1");
//            mUpdateView.showAfterDeviceVersion(device.getInstanceNumber() + "--" + version);
//        } else {
        mAfterRemoteDevice.add(device);
//        String version = Public.readVersion(device);

//            mUpdateView.showUpgradeInformation("found device：" + device.getInstanceNumber() + "    " + count + "/" + mUpdateView.getOriginalSize());
//            mUpdateView.showAfterDeviceVersion(device.getInstanceNumber() + "--" + version);
//        }
    }

    @Override
    public void findOriginDevice(int flag) {
        originDeviceFlag = flag;
        synchronized (lockOrigin) {
            lockOrigin.notify();
        }
    }

    public void findBeforeDevice() {
        originDeviceFlag = Common.DEVICE_FOUND_ALL;
        synchronized (lockBefore) {
            lockBefore.notify();
        }
    }

    public void cancelListener() {
        localDevice.getEventHandler().removeListener(upgradeRemoteListener);
    }

    private void changeUpgradeFirmware(){
        int typeNum = mFirmWareInformation.getTypeNum();
        switch (typeNum){
            case 1:
                mFileTmp=mFileTmp2;
                mFirmWareInformation=mFirmWareInformation2;
                break;
            case 2:
                mFileTmp=mFileTmp1;
                mFirmWareInformation=mFirmWareInformation1;
                break;
        }
    }

    @Override
    public String getFirmWareModelName() {
        return conf.getModelName();
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
                byte[] buffer = new byte[blockSize];
                if (startOffset + blockSize > mFileTmp.length) {
                    System.arraycopy(mFileTmp, startOffset, buffer, 0, mFileTmp.length - startOffset);
                } else {
                    System.arraycopy(mFileTmp, startOffset, buffer, 0, blockSize);
                }

                list.add(new OctetString(buffer));
                return new ConfirmedPrivateTransferAck(vendorId, serviceNumber, list);
        }
        return null;
    }
}
