package listener;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import common.Common;
import update.IUpgrade;
import util.MyLocalDevice;
import util.Public;
import util.STExecutor;

import java.util.ArrayList;
import java.util.List;

public class UpgradeRemoteListener extends DeviceEventAdapter {

    private final IUpgrade mUpgrade;

    /**
     * 找到指定modelname的设备
     */
    boolean isFound = false;

    /**
     * 记录原始设备的个数，若和sourceAddress相等，则说明找全设备
     */
    private int originCount = 0;

    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList = new ArrayList<>();
    private boolean Debug = false;

    public UpgradeRemoteListener(IUpgrade upgrade) {
        this.mUpgrade = upgrade;
    }

    @Override
    public void iAmReceived(RemoteDevice d) {
        super.iAmReceived(d);
        Integer id = Integer.valueOf(d.getInstanceNumber());
        boolean exist = remoteDeviceIDList.contains(id);
        if (exist) {
            System.out.println("exits id: "+id);
            return;
        }
        System.out.println("id: "+id);
        remoteDeviceIDList.add(id);
        STExecutor.submit(new FilterRemoteDevice(d));
    }

    class FilterRemoteDevice implements Runnable {

        RemoteDevice d;

        public FilterRemoteDevice(RemoteDevice d) {
            this.d = d;
        }

        @Override
        public void run() {
            //若flag为升级后 的状态，则更新Mylocaldevice
            if (mUpgrade.getFlag() == 3) {
                try {
                    String reg = Public.getAllString(mUpgrade.getFirmWareType(), "[A-za-z0-9-/]");
                    String firmWareModelName = mUpgrade.getFirmWareModelName();
                    String[] modelNames = firmWareModelName.split(",");
                    boolean match = false;
                    for(int i=0;i<modelNames.length;i++){
                        if (Public.matchString(Public.readModelName(d), modelNames[i])) {
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        isFound = true;
                        mUpgrade.addJListDevice(d);
                        MyLocalDevice.updateRemoteDevice(d);

                    }
                } catch (BACnetException e) {
                    e.printStackTrace();
                }
            } else if (mUpgrade.getFlag() == 1 || mUpgrade.getFlag() == 2) {
                String reg = Public.getAllString(mUpgrade.getFirmWareType(), "[A-za-z0-9-/]");
                String[] split = mUpgrade.getFirmWareModelName().split(",");
                boolean match = false;
                for (int i = 0; i < split.length; i++) {
                    if (Public.matchString(Public.readModelName(d), split[i])) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    isFound = true;
                    mUpgrade.addJListDevice(d);
                }
            }

            //判断原始数据阶段是否找全所有设备
            if (mUpgrade.getFlag() == 1) {
                originCount++;
//                System.out.println("总个数 "+MyLocalDevice.getAddressList().size()+"/"+originCount);
                if (MyLocalDevice.getAddressList().size() == originCount) {
                    if (isFound) {
                        mUpgrade.findOriginDevice(Common.DEVICE_FOUND_ALL);
                        isFound = false;
                    } else {
//                        mUpdateView.showConfirmDialog("no device found! upgrade exit!");
                    }
                    if (Debug) {
//                        mUpdateView.showUpgradeInformation("-----+  origin 找到所有电机");
                    }
                }
            }
        }
    }


    public void clearRemoteDeviceList() {
        originCount = 0;
        this.remoteDeviceIDList.clear();
    }
}
