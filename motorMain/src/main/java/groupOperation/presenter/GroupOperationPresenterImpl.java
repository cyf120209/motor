package groupOperation.presenter;

import groupOperation.view.GroupOperationView;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import listener.AnnounceListener;
import model.DeviceGroup;
import util.MyLocalDevice;
import util.Draper;
import util.DraperSubItem;
import util.DraperSubList;

import java.util.*;

/**
 * Created by lenovo on 2017/1/19.
 */
public class GroupOperationPresenterImpl implements GroupOperationPresenter {

    private static GroupOperationPresenterImpl groupOperationPresenterImpl;
    private AnnounceListener announceListener;
    private GroupOperationView mGroupOperationView;
    private LocalDevice localDevice;
    List<RemoteDevice> mRemoteDevices;

    /**
     * announce类型 1：代表全部，2：代表组
     */
    private int mAnnounceType = 1;

    /**
     * serviceParameters参数列表，主要防止重复添加
     */
    private List<Sequence> mSequence = new ArrayList<>();

    /**
     * 设备和组和电机的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();

    /**
     * 设备行点击的电机组列表
     */
    private List<Integer> mGroup = new ArrayList<>();

    /**
     * 设备和组的关系列表
     */
    private Map<Integer, List<Integer>> mGroupDraper = new HashMap<>();
    private Integer mDeviceID;
    private Integer mGroupIndex;

    public GroupOperationPresenterImpl() {
    }

    public GroupOperationPresenterImpl(GroupOperationView mGroupOperationView) {
        groupOperationPresenterImpl=this;
        this.mGroupOperationView = mGroupOperationView;
        localDevice= MyLocalDevice.getInstance();
//        mRemoteDevices = localDevice.getRemoteDevices();
//        List<String> draperList=new ArrayList<String>();
//        for (RemoteDevice d:mRemoteDevices){
//            draperList.add(""+d.getInstanceNumber());
//        }
//        if(draperList!=null && draperList.size()>0){
//            Object[] objects = draperList.toArray();
//            String[] drapers = (String[]) draperList.toArray(new String[draperList.size()]);
//            mGroupOperationView.updateDraper(drapers);
//        }
        announceListener = new AnnounceListener(localDevice, this, mGroupOperationView);
        localDevice.getEventHandler().addListener(announceListener);
        AnnounceSubbutton();
    }

    public static void announce(){
        groupOperationPresenterImpl.AnnounceSubbutton();
    }

    @Override
    public String[] getGroups(int index) {
        mDeviceID = mDevicesIDList.get(index);
        mGroupDraper = mRelativeList.get(mDeviceID);
        Iterator<Map.Entry<Integer, List<Integer>>> iterator = mGroupDraper.entrySet().iterator();
        mGroup.clear();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> next = iterator.next();
            mGroup.add(next.getKey());
        }
        if(mGroup.size()==0){
            return null;
        }
        String[] groups=new String[mGroup.size()];
        for (int i=0;i<mGroup.size();i++){
            groups[i]=""+mGroup.get(i);
        }
        return groups;
    }

    @Override
    public String[] getDrapers(int index) {
        mGroupIndex = mGroup.get(index);
        List<Integer> draperList = mGroupDraper.get(mGroupIndex);
        String[] drapers=new String[draperList.size()];
        for (int i=0;i<draperList.size();i++) {
            drapers[i]=""+draperList.get(i);
        }
        return drapers;
    }

    @Override
    public void updateGroups() {

    }

    @Override
    public void updateDrapers() {

    }

    @Override
    public void AnnounceSubbutton() {
        if(localDevice!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSequence.clear();
                        mRelativeList.clear();
                        mDevicesIDList.clear();
                        Draper.sendAnnounce();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void addSelGroup() {
//        if(localDevice!=null){
//            try {
//                Draper.sendGroupSubscriptionToSelect(mRemoteDevices.get(mGroupOperationView.getdevBoxSelectedItem()),false,mGroupOperationView.getDeviceNum(),mGroupOperationView.getGroupNum());
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    @Override
    public void addAllGroup() {
//        if(localDevice!=null){
//            try {
//                Draper.sendGroupSubscription(false,mGroupOperationView.getDeviceNum(),mGroupOperationView.getGroupNum());
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    @Override
    public void delSelGroup() {
//        if(localDevice!=null){
//            try {
//                Draper.sendGroupSubscriptionToSelect(mRemoteDevices.get(mGroupOperationView.getdevBoxSelectedItem()),true,mGroupOperationView.getDeviceNum(),mGroupOperationView.getGroupNum());
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    @Override
    public void delAllGroup() {
//        if(localDevice!=null){
//            try {
//                Draper.sendGroupSubscription(true,mGroupOperationView.getDeviceNum(),mGroupOperationView.getGroupNum());
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    @Override
    public void upBt() {
        try {
            Draper.sendCmd(mDeviceID, mGroupIndex, Draper.DRAPER_CMD_RETRACTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        STExecutor.sendCmd(localDevice, mDeviceID, mGroupIndex, Draper.DRAPER_CMD_RETRACTED);
    }

    @Override
    public void stopButton() {
        try {
            Draper.sendCmd( mDeviceID, mGroupIndex, Draper.DRAPER_CMD_STOP);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void downbt() {
        try {
            Draper.sendCmd( mDeviceID, mGroupIndex, Draper.DRAPER_CMD_EXTENDED);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void cancelListener() {
        localDevice.getEventHandler().removeListener(announceListener);
    }

    @Override
    public synchronized void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters) {
        Sequence parms = serviceParameters;

        if (serviceNumber.intValue() == 7) {
            if (mAnnounceType == 1) {
                deviceAnnounce(parms);
            } else {
                groupAnnounce(parms);
            }
        }
    }

    private void groupAnnounce(Sequence parms) {
        Map<String, Encodable> values1 = parms.getValues();
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        List<Integer> list = new ArrayList<>();
        list.add(instanceNumber1);
    }

    private void deviceAnnounce(Sequence parms) {
        //parms 去重处理
        if (mSequence.contains(parms)) {
            return;
        }
        List<DeviceGroup> deviceGroupList=new ArrayList<>();
        mSequence.add(parms);
        Map<String, Encodable> values1 = parms.getValues();
        //获取draperID
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        //该draperID下的设备-组关系
        DraperSubList deviceGroup1 = (DraperSubList) values1.get("DeviceGroup");
        for (DraperSubItem item1 : deviceGroup1.getList()) {
            Map<Integer, List<Integer>> CdevGrpInf = null;
            int deviceId = item1.getDevicID().getInstanceNumber();
            CdevGrpInf = mRelativeList.get(deviceId);
            if (CdevGrpInf == null) {
                CdevGrpInf = new HashMap<Integer, List<Integer>>();
                mRelativeList.put(deviceId, CdevGrpInf);
            }
            if (!mDevicesIDList.contains(deviceId)) {
                mDevicesIDList.add(deviceId);
            }
            List<Integer> devList = null;
            int groupId = item1.getGroupID().intValue();
            devList = CdevGrpInf.get(groupId);
            if (devList == null) {
                devList = new LinkedList<Integer>();
                CdevGrpInf.put(groupId, devList);
            }
            if (!devList.contains(instanceNumber1)) {
                devList.add(instanceNumber1);
            }
            deviceGroupList.add(new DeviceGroup(deviceId,groupId));
        }
        MyLocalDevice.mRemoteUtils.setRelationMap(mRelativeList);
        MyLocalDevice.mRemoteUtils.setDeviceGroupList(deviceGroupList);
        mGroupOperationView.updateExistedGroup(mDevicesIDList.toArray());
        mGroupOperationView.updateDevice(mDevicesIDList.toArray());
    }
}
