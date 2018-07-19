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
import schedule.model.ScheduleModelImpl;
import schedule.view.IScheduleOnly;
import util.Draper;
import util.DraperSubItem;
import util.DraperSubList;
import util.MyLocalDevice;

import java.util.*;

public class SchedulePresenterImpl implements ISchedulePresenter{

//    private final ScheduleListener mScheduleListener;
    private final IScheduleModel mScheduleModel;
    private final List<Schedule> schedules;
    private LocalDevice localDevice;
//    private SuntrackingListener mSuntrackingListener;
    private IScheduleOnly mScheduleView;

    /**
     * 设备和组和电机的关系列表
     */
    private Map<Integer, Map<Integer, List<Integer>>> mRelativeList = new HashMap<>();

    /**
     * 设备ID列表
     */
    private List<Integer> mDevicesIDList = new ArrayList<>();

    public SchedulePresenterImpl(IScheduleOnly scheduleView) {
        this.mScheduleView = scheduleView;
        localDevice = MyLocalDevice.getInstance();
        mScheduleModel = new ScheduleModelImpl();

        ScheduleDao scheduleDao = new ScheduleDao();
        schedules = scheduleDao.queryAll();
        mScheduleView.showSchedule(schedules);
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
                str,
                schedule.getScheduleName());
    }

    @Override
    public long insert(view.Schedule schedule) {
        Schedule schedule1 = viewSchedule2entitySchedule(schedule);
        long insert = mScheduleModel.insert(schedule1);
        schedules.add(schedule1);
        return insert;
    }

    @Override
    public int update(int index,view.Schedule schedule) {
        Schedule schedule2Update = viewSchedule2entitySchedule(schedule);
        Schedule scheduleOrigin = schedules.get(index);
        schedule2Update.setId(scheduleOrigin.getId());
        int update = mScheduleModel.update(schedule2Update);
        if(update>0){
            schedules.set(index,schedule2Update);
        }
        return update;
    }

    @Override
    public int delete(int index) {
        int delete = mScheduleModel.delete(schedules.get(index).getId());
        if(delete>0){
            schedules.remove(index);
        }
        return delete;
    }

    @Override
    public int delete(List<Integer> idList) {
//        List<Integer> delId=new ArrayList<>();
//        for (int i = 0; i < idList.size(); i++) {
//            Schedule schedule = schedules.get(i);
//            int id = schedule.getId();
//            delId.add(id);
//        }
//        return mScheduleModel.delete(delId);
        return 0;
    }

    @Override
    public int deleteAll() {
        return mScheduleModel.deleteAll();
    }

}
