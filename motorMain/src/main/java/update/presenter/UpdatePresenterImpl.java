package update.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import listener.UpdateListener;
import util.MyLocalDevice;
import update.view.UpdateView;
import util.Draper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/1/20.
 */
public class UpdatePresenterImpl implements UpdatePresenter {

    private final List<RemoteDevice> mRemoteDevices;
    private final Map<Integer, RemoteDevice> mRemoteDeviceMap;
    private UpdateListener updateListener;
    private UpdateView mUpdateView;
    private File framefile;
    private byte[] fileTmp;
    private LocalDevice localDevice;
    private String type;
    private boolean isSendCompleted = true;

    /**
     * 升级进度
     */
    private int percent;
    private int mTotalSize;

    Object lock = new Object();
    /**
     * 是否取消升级
     */
    private boolean isCancel;

    /**
     * 升级日志显示 1.代表原始 2.代表升级前 3.代表升级后
     */
    private int flag = 1;

    public UpdatePresenterImpl(UpdateView mUpdateView) {
        this.mUpdateView = mUpdateView;
        localDevice = MyLocalDevice.getInstance();
        mRemoteDevices = localDevice.getRemoteDevices();
        mRemoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        mUpdateView.updateDevBox(mRemoteDevices);
        updateListener = new UpdateListener(localDevice, this, mUpdateView);
        localDevice.getEventHandler().addListener(updateListener);
        try {
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
//            localDevice.sendGlobalBroadcast(localDevice.getIAm());
        } catch (BACnetException e) {
            e.printStackTrace();
        }
//        new UpdatePercent().run();

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
            framefile = fDialog.getSelectedFile();
            CheckFileType(framefile);
            ReadFileTobuff();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(framefile.lastModified());
            String s = sdf.format(cal.getTime());
            mUpdateView.updateLastModify(s);
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

            if (startAddress < 0x0080) {
                mUpdateView.updateTypeLabelText("0");
            } else if (startAddress < 0x0240) {
                mUpdateView.updateTypeLabelText("1");
            } else {
                mUpdateView.updateTypeLabelText("2");
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
    private void ReadFileTobuff() {
        try {
            fileTmp = new byte[(int) framefile.length()];
            FileInputStream fi = new FileInputStream(framefile);
            int offset = 0, numRead = 0;
            while (offset < fileTmp.length
                    && (numRead = fi.read(fileTmp, offset, fileTmp.length - offset)) >= 0) {
                offset += numRead;
            }
            String s = new String(fileTmp);
            boolean right = false;
            String name = getString(s, "--\\{DARPER\\sFIREWARE\\}--");
//            String type = getString(s, "\\[[a-zA-Z]{2}-[a-zA-Z]{2}-[0-9]{2}\\]");
            type = getString(s, "--\\[[A-Za-z0-9-/]*\\]--");
            type = type.substring(3, type.length() - 3);
            String version = getString(s, "--\\*v[0-9]+.[0-9]+.[0-9]+\\*--");
            version = version.substring(4, version.length() - 3);
            String[] split = version.split("\\.");
            if ("--{DARPER FIREWARE}--".equals(name) && !"".equals(type) && !"".equals(version)) {
                right = true;
            }
            if (right) {
                mUpdateView.updateVersionAndType(type, split[0], split[1].trim(), split[2]);
            }
            fi.close();
        } catch (Exception e1) {
            mUpdateView.showError("Firm corrupt!");
            e1.printStackTrace();
        }
    }

    /**
     * 正则匹配
     *
     * @param str
     * @param regx
     * @return
     */
    public String getString(String str, String regx) {
        //1.将正在表达式封装成对象Patten 类来实现
        Pattern pattern = Pattern.compile(regx);
        //2.将字符串和正则表达式相关联
        Matcher matcher = pattern.matcher(str);
        //3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
        System.out.println(matcher.matches());
        String group = "";
        //查找符合规则的子串
        while (matcher.find()) {
            //获取 字符串
            group = matcher.group();
            //获取的字符串的首位置和末位置
//            System.out.println(matcher.start() + "--" + matcher.end());
        }
        return group;
    }

    @Override
    public boolean getUpdateState() {
        return isSendCompleted;
    }

    /**
     * 读取版本号
     *
     * @param remoteDevice
     * @return
     */
    @Override
    public synchronized String ReadVersion(RemoteDevice remoteDevice) {
        try {
//            mUpdateView.updateVersionLabel("Version: NULL");
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.firmwareRevision));
//            mUpdateView.updateVersionLabel("Version:   " + ack.getValue().toString());
            return ack.getValue().toString();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 读取版本号
     *
     * @param remoteDevice
     * @return
     */
    public synchronized String readModelName(RemoteDevice remoteDevice) {
        try {
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.modelName));
            return ack.getValue().toString();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    @Override
    public void ReadValue() {
        try {
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
//            localDevice.sendGlobalBroadcast(localDevice.getIAm());
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新日志
     *
     * @param device
     */
    @Override
    public synchronized void addJListDevice(final RemoteDevice device) {
        if (flag != 1 && !readModelName(device).equals(type)) {
            return;
        }
        if (flag == 1) {
            String version = ReadVersion(device);
            mUpdateView.showOriginalDeviceVersion(device.getInstanceNumber() + "--" + version);

        } else if (flag == 2) {
            String version = ReadVersion(device);
            mUpdateView.showBeforeDeviceVersion(device.getInstanceNumber() + "--" + version);

        } else if (flag == 3) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            String version = ReadVersion(device);
            mUpdateView.showAfterDeviceVersion(device.getInstanceNumber() + "--" + version);
//                }
//            }).start();
        }
    }

    @Override
    public void update(boolean isCancel) {
        synchronized (lock) {
            this.isCancel = isCancel;
            if (isCancel) {
                flag = 1;
                isSendCompleted=true;
            } else {
                flag = 3;
            }
            lock.notify();
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
        if (framefile == null) {
            framefile = new File(mUpdateView.getFileName());
        }
        //重置进度条
        percent = 0;
        flag = 2;
        isSendCompleted = false;
        try {
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            Thread.sleep(100);
        } catch (BACnetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mUpdateView.updateProgress(0);
        new Thread(new UpdatePresenterImpl.RunUpdateToOne(framefile, localDevice, (mUpdateView.getdevBoxSelectedItem()))).start();
    }

    @Override
    public void updateButton() {
        try {
            if (framefile == null) {
                framefile = new File(mUpdateView.getFileName());
            }
            //重置进度条
            percent = 0;
            flag = 2;
            isSendCompleted = false;
            try {
                localDevice.sendGlobalBroadcast(new WhoIsRequest());
                Thread.sleep(100);
            } catch (BACnetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mUpdateView.updateProgress(0);
            new Thread(new UpdatePresenterImpl.RunUpdate(framefile, localDevice)).start();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public byte[] getfileTmp() {
        return fileTmp;
    }

    @Override
    public String getFileName() {
        return mUpdateView.getFileName();
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
                Draper.sendIHaveFrameToOne(type, peer, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), mUpdateView.getTypeNum(), updateFile);
                //确保升级前找到所有设备，否则不升级,如果升级前找到了所以设备，则升级，否则进入等待
                Thread.sleep(6000);
                if (mUpdateView.getOriginalSize() != mUpdateView.getBeforeSize()) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                //弹出确认升级对话框
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateView.showConfirmDialog();
                    }
                }).start();
                synchronized (lock) {
                    lock.wait();
                }
                if (isCancel) {
                    return;
                }
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
                    throw new IOException("Could not completely read file "
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
                    Draper.sendFrameWareToOne(type, peer, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), buffer.length, sent, data);
                sent = sent + 256;
            } else {
                byte[] data1 = new byte[buffer.length - sent];
                System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                if (localDevice != null)
                    Draper.sendFrameWareToOne(type, peer, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), buffer.length, sent, data1);
                sent = buffer.length;
            }
//            Thread.sleep(200);
//            percent = sent * 100 / buffer.length;
            return sent;
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
                Draper.sendIHaveFrame(type, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), mUpdateView.getTypeNum(), updateFile);
                //确保升级前找到所有设备，否则不升级,如果升级前找到了所以设备，则升级，否则进入等待
                Thread.sleep(6000);
                if (mUpdateView.getOriginalSize() != mUpdateView.getBeforeSize()) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                //弹出确认升级对话框
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateView.showConfirmDialog();
                    }
                }).start();
                synchronized (lock) {
                    lock.wait();
                }
                if (isCancel) {
                    return;
                }
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
                    throw new IOException("Could not completely read file "
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

        private void setBuffer(byte[] buffer, int sent) throws BACnetException {
            while (sent < buffer.length) {
                if (buffer.length - sent > 256) {
                    byte[] data = new byte[256];
                    System.arraycopy(buffer, sent, data, 0, 256);
                    if (localDevice != null)
                        Draper.sendFrameWare(type, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), buffer.length, sent, data);
                    sent = sent + 256;
                } else {
                    byte[] data1 = new byte[buffer.length - sent];
                    System.arraycopy(buffer, sent, data1, 0, buffer.length - sent);
                    if (localDevice != null)
                        Draper.sendFrameWare(type, mUpdateView.getMajorNum(), mUpdateView.getMinorNum(), mUpdateView.getPatchNum(), buffer.length, sent, data1);
                    sent = buffer.length;
                }
//                        Thread.sleep(200);
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
                    System.out.println("UpdatePresenterImpl frameToSend.size" + frameToSend.size());
                    percent = 100 - frameToSend.size() * 100 / mTotalSize;
                    mUpdateView.updateProgress(percent);
                    Timer t = (Timer) e.getSource();
                    // 如果进度条达到最大值重新开发计数
                    if (percent == 100) {
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
                    ReadFileTobuff();
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
    public void findAllDevice() {
        synchronized (lock) {
            lock.notify();
        }
    }
}
