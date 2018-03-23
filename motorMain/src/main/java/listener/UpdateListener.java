package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import common.Common;
import update.presenter.UpdatePresenterImpl;
import util.MyLocalDevice;
import update.presenter.UpdatePresenter;
import update.view.UpdateView;
import util.Public;
import util.STExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/19.
 */
public class UpdateListener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private UpdatePresenter mUpdatePresenter;
    UpdateView mUpdateView;
    boolean isFirst = true;

    boolean Debug=false;

    boolean isFound=false;

    /**
     * 记录原始设备的个数，若和sourceAddress相等，则说明找全设备
     */
    private int originCount = 0;

    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList = new ArrayList<>();

    public UpdateListener(LocalDevice localDevice, UpdatePresenter mUpdatePresenter, UpdateView updateView) {
        this.localDevice = localDevice;
        this.mUpdatePresenter = mUpdatePresenter;
        this.mUpdateView = updateView;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
        Integer id = Integer.valueOf(d.getInstanceNumber());
        boolean exist = remoteDeviceIDList.contains(id);
        if (exist) {
            return;
        }
        remoteDeviceIDList.add(id);
        if(Debug){
            mUpdateView.showUpgradeInformation("IDList.size" + remoteDeviceIDList.size() + "flag:" + mUpdatePresenter.getFlag());
        }
        if (listener != null && remoteDeviceIDList.size() == MyLocalDevice.getAddressList().size()) {
            listener.received();
            if(Debug){
                mUpdateView.showUpgradeInformation("who is 找齐 " + mUpdatePresenter.getFlag());
            }
        }

        //更新升级列表
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //若flag为升级后 的状态，则更新Mylocaldevice
                if (mUpdatePresenter.getFlag() == 3) {
                    try {
                        String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z0-9-/]");
                        //墙面开关
                        if(Public.matchString(reg, "^WS-")){
//                        mUpdateView.showUpgradeInformation("-墙面开关");
                            String substring = reg.substring(3);
                            String[] split = substring.split("/");
                            boolean match=false;
                            for (int i=0;i<split.length;i++){
                                if(Public.matchString(Public.readModelName(d), "WS-"+split[i])){
                                    match=true;
                                    break;
                                }
                            }
                            if(match){
                                MyLocalDevice.updateRemoteDevice(d);
                                mUpdatePresenter.addJListDevice(d);
                            }
                        }else {
                            if (Public.matchString(Public.readModelName(d), reg)) {
                                mUpdateView.updateDevBox(d);
                                MyLocalDevice.updateRemoteDevice(d);
                                mUpdatePresenter.addJListDevice(d);
                            }
                        }
                    } catch (BACnetException e) {
                        e.printStackTrace();
                    }
                } else if (!UpdatePresenterImpl.isSingle && mUpdatePresenter.getFlag() != 3) {
                    String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z0-9-/]");
//                    mUpdateView.showUpgradeInformation(reg);
                    //墙面开关
                    if(Public.matchString(reg, "^WS-")){
//                        mUpdateView.showUpgradeInformation("-墙面开关");
                        String substring = reg.substring(3);
                        String[] split = substring.split("/");
                        boolean match=false;
                        for (int i=0;i<split.length;i++){
                            if(Public.matchString(Public.readModelName(d), "WS-"+split[i])){
                                match=true;
                                break;
                            }
                        }
                        if(match){
                            isFound=true;
                            mUpdatePresenter.addJListDevice(d);
                        }
                    }else {
                        if (Public.matchString(Public.readModelName(d), reg)) {
                            isFound=true;
                            mUpdatePresenter.addJListDevice(d);
                            //mUpdateView.showUpgradeInformation(" ----------------send to jList");
                        }
                    }
                } else if (UpdatePresenterImpl.isSingle && mUpdatePresenter.getFlag() != 3) {
                    String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z0-9-/]");
//                    mUpdateView.showUpgradeInformation(reg);
                    if (mUpdateView.getdevBoxSelectedItem().equals(d)) {
                        //墙面开关
                        if(Public.matchString(reg, "^WS-")){
                        mUpdateView.showUpgradeInformation("墙面开关");
                            String substring = reg.substring(3);
                            String[] split = substring.split("/");
                            boolean match=false;
                            for (int i=0;i<split.length;i++){
                                if(Public.matchString(Public.readModelName(d), "WS-"+split[i])){
                                    match=true;
                                    break;
                                }
                            }
                            if(match){
                                isFound=true;
                                mUpdatePresenter.addJListDevice(d);
                            }
                        }else {
                            mUpdateView.showUpgradeInformation("其他");
                            if (Public.matchString(Public.readModelName(d), reg)) {
                                isFound=true;
                                mUpdatePresenter.addJListDevice(d);
                            }
                        }
                    }
                }

                //判断原始数据阶段是否找全所有设备
                if (mUpdatePresenter.getFlag() == 1) {
                    originCount++;
//                    mUpdateView.showUpgradeInformation("总个数 "+MyLocalDevice.getAddressList().size()+"/"+originCount);
                    if (!UpdatePresenterImpl.isSingle && MyLocalDevice.getAddressList().size() == originCount) {
                        if(isFound){
                            mUpdatePresenter.findOriginDevice(Common.DEVICE_FOUND_ALL);
                            isFound=false;
                        }else {
                            mUpdateView.showConfirmDialog("no device found! upgrade exit!");
                        }
                        if(Debug) {
                            mUpdateView.showUpgradeInformation("-----+  origin 找到所有电机");
                        }
                    }
                }
            }
        };
        STExecutor.submit(runnable);
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        System.out.println("111");
        Sequence parms = (Sequence) serviceParameters;
        if (serviceNumber.intValue() == 7) {
            for (int i = 0; i < 2; i++) {
                String RC = parms.getValues().get("Device RC" + i).toString();
                String GP = parms.getValues().get("Device GP" + i).toString();
                System.out.println(RC + "----" + GP);
            }
        }
    }

    @Override
    public AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address) {
        return mUpdatePresenter.privateTransferReceivedComplex(vendorId, serviceNumber, serviceParameters, address);
    }

    public void clearRemoteDeviceList() {
        originCount = 0;
        isFound=false;
        this.remoteDeviceIDList.clear();
    }

    ReceivedListener listener;

    public void setListener(ReceivedListener listener) {
        this.listener = listener;
    }

    public interface ReceivedListener {
        void received();
    }
}