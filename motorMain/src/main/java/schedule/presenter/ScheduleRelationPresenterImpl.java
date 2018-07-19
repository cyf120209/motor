package schedule.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import dao.GroupDao;
import dao.ScheduleDao;
import entity.Schedule;
import entity.ScheduleGroupRelation;
import entity.ShadeGroup;
import listener.ScheduleListener;
import model.DeviceGroup;
import schedule.model.IScheduleModel;
import schedule.model.IScheduleRelationModel;
import schedule.model.ScheduleModelImpl;
import schedule.model.ScheduleRelationModelImpl;
import schedule.view.IScheduleRelation;
import util.Draper;
import util.DraperSubItem;
import util.DraperSubList;
import util.MyLocalDevice;

import java.util.*;

public class ScheduleRelationPresenterImpl implements IScheduleRelationPresenter{

    private IScheduleRelation mScheduleRelation;
    private List<Schedule> mScheduleList=new ArrayList<>();
    private final ScheduleListener mScheduleListener;
    private final IScheduleRelationModel mScheduleRelationModel;
    private LocalDevice localDevice;
    //    private SuntrackingListener mSuntrackingListener;

    /**
     * 设备和组和电机的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();

    public ScheduleRelationPresenterImpl(IScheduleRelation scheduleRelation) {
        this.mScheduleRelation = scheduleRelation;
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        localDevice = MyLocalDevice.getInstance();
        mScheduleRelationModel = new ScheduleRelationModelImpl();
        mScheduleListener = new ScheduleListener(this);
        localDevice.getEventHandler().addListener(mScheduleListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    sendAnnounce();
                    mSequence.clear();
                    mRelativeList.clear();
                    mDevicesIDList.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void init(){
        ScheduleDao scheduleDao = new ScheduleDao();
        mScheduleList = scheduleDao.queryAll();
        mScheduleRelation.showSchedule(mScheduleList);

        try {
            Draper.sendAnnounce();
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    /**
     * announce类型 1：代表全部，2：代表组
     */
    private int mAnnounceType = 1;

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

    /**
     * serviceParameters参数列表，主要防止重复添加
     */
    private List<Sequence> mSequence = new ArrayList<>();

    private void deviceAnnounce(Sequence parms) {
        //parms 去重处理
        if (mSequence.contains(parms)) {
            return;
        }
        mSequence.add(parms);
        Map<String, Encodable> values1 = parms.getValues();
        //获取draperID
        ObjectIdentifier draperID1 = (ObjectIdentifier) values1.get("draperID");
        int instanceNumber1 = draperID1.getInstanceNumber();
        //该draperID下的设备-组关系
        DraperSubList deviceGroup1 = (DraperSubList) values1.get("DeviceGroup");
        for (DraperSubItem item1 : deviceGroup1.getList()) {
            Map<Integer, List<Integer>> CdevGrpInf = null;
            CdevGrpInf = mRelativeList.get(item1.getDevicID().getInstanceNumber());
            if (CdevGrpInf == null) {
                CdevGrpInf = new HashMap<Integer, List<Integer>>();
                mRelativeList.put(item1.getDevicID().getInstanceNumber(), CdevGrpInf);
            }
            if (!mDevicesIDList.contains(item1.getDevicID().getInstanceNumber())) {
                mDevicesIDList.add(item1.getDevicID().getInstanceNumber());
            }
            List<Integer> devList = null;
            devList = CdevGrpInf.get(item1.getGroupID().intValue());
            if (devList == null) {
                devList = new LinkedList<Integer>();
                CdevGrpInf.put(item1.getGroupID().intValue(), devList);
            }
            if (!devList.contains(instanceNumber1)) {
                devList.add(instanceNumber1);
            }
        }
        MyLocalDevice.mRemoteUtils.setRelationMap(mRelativeList);
        mScheduleRelation.updateExistedGroup(mDevicesIDList.toArray());
    }

    @Override
    public void sendAnnounce() {
        try {
            Draper.sendAnnounce();
            mSequence.clear();
            mRelativeList.clear();
            mDevicesIDList.clear();
            System.out.println("send Anounce");
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Schedule getGroupListByScheduleId(int scheduleId) {
        return mScheduleRelationModel.getGroupListByScheduleId(scheduleId);
    }

    @Override
    public int saveScheduleGroup(int scheduleId, List<DeviceGroup> addGroupList, List<DeviceGroup> delGroupList) {

        GroupDao groupDao = new GroupDao();
        List<ShadeGroup> shadeGroupList = groupDao.queryAll();
        Schedule schedule = mScheduleList.get(scheduleId);
        if(addGroupList!=null && addGroupList.size()>0){
            addScheduleGroup(schedule,shadeGroupList,addGroupList);
        }
        if(delGroupList!=null && delGroupList.size()>0){
            delScheduleGroup(schedule,shadeGroupList,delGroupList);
        }
        return 1;
    }


    private int addScheduleGroup(Schedule schedule,List<ShadeGroup> shadeGroupList,List<DeviceGroup> addGroupList){
        List<ScheduleGroupRelation> scheduleGroupRelationList=new ArrayList<>();
        for (DeviceGroup deviceGroup: addGroupList) {
            for (ShadeGroup shadeGroup:shadeGroupList){
                if(deviceGroup.getDeviceId().intValue()==shadeGroup.getDeviceId().intValue() &&
                        deviceGroup.getGroupId().intValue()==shadeGroup.getGroupId().intValue() &&
                        deviceGroup.getGroupName().equals(shadeGroup.getGroupName())){
                    ScheduleGroupRelation scheduleGroupRelation = new ScheduleGroupRelation(shadeGroup.getId(),schedule.getId());
                    scheduleGroupRelationList.add(scheduleGroupRelation);
                }
            }
        }
        return mScheduleRelationModel.addScheduleGroup(scheduleGroupRelationList);
    }

    private int delScheduleGroup(Schedule schedule,List<ShadeGroup> shadeGroupList,List<DeviceGroup> delGroupList){
        List<ScheduleGroupRelation> scheduleGroupRelationList=new ArrayList<>();
        for(DeviceGroup deviceGroup:delGroupList){

            for (ShadeGroup shadeGroup:shadeGroupList){
                if(deviceGroup.getDeviceId().intValue()==shadeGroup.getDeviceId().intValue() &&
                        deviceGroup.getGroupId().intValue()==shadeGroup.getGroupId().intValue() &&
                        deviceGroup.getGroupName().equals(shadeGroup.getGroupName())){
                    ScheduleGroupRelation scheduleGroupRelation = new ScheduleGroupRelation(shadeGroup.getId(),schedule.getId());
                    scheduleGroupRelationList.add(scheduleGroupRelation);
                }
            }
        }
        return mScheduleRelationModel.delScheduleGroup(scheduleGroupRelationList);
    }

}
