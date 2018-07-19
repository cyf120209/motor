package schedule.view;

import entity.Schedule;
import model.DeviceGroup;

import java.util.List;

public interface IScheduleRelation {

    void updateExistedGroup(Object[] groupList);

    DeviceGroup getDeviceGroup();

    void showSchedule(List<Schedule> scheduleList);
}
