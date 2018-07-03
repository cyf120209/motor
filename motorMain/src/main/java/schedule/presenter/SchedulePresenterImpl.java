package schedule.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import dao.ScheduleDao;
import entity.Schedule;
import listener.ScheduleListener;
import model.DeviceGroup;
import schedule.model.IScheduleModel;
import schedule.model.ScheduleModelImpl;
import schedule.view.IScheduleView;
import util.Draper;
import util.DraperSubItem;
import util.DraperSubList;
import util.MyLocalDevice;

import java.util.*;

public class SchedulePresenterImpl implements ISchedulePresenter{

    private final ScheduleListener mScheduleListener;
    private final IScheduleModel mScheduleModel;
    private LocalDevice localDevice;
//    private SuntrackingListener mSuntrackingListener;
    private IScheduleView mScheduleView;

    /**
     * 设备和组和电机的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();

    public SchedulePresenterImpl(IScheduleView scheduleView) {
        this.mScheduleView = scheduleView;
        localDevice = MyLocalDevice.getInstance();
        mScheduleModel = new ScheduleModelImpl();
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
        ScheduleDao scheduleDao = new ScheduleDao();
        List<Schedule> schedules = scheduleDao.queryAll();
        mScheduleView.showSchedule(schedules);
    }

    @Override
    public DeviceGroup getDeviceGroup() {
        return null;
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
        mScheduleView.updateExistedGroup(mDevicesIDList.toArray());
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

    private Schedule viewSchedule2entitySchedule(view.Schedule schedule) {
        List<String> week = schedule.getWeek();
        String str="";
        for (String w:week){
            str+=(w+",");
        }
        return new Schedule(schedule.getHour(),
                schedule.getMin(),
                schedule.getPercent(),
                str);
    }

    @Override
    public void insert(view.Schedule schedule) {
        Schedule schedule1 = viewSchedule2entitySchedule(schedule);
        mScheduleModel.insert(schedule1);
    }

    @Override
    public void update(view.Schedule schedule) {
        mScheduleModel.update(viewSchedule2entitySchedule(schedule));
    }

    @Override
    public void delete(List<Integer> idList) {
        mScheduleModel.delete(idList);
    }

    @Override
    public void deleteAll() {
        mScheduleModel.deleteAll();
    }
}
