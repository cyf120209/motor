package schedule.view;

import entity.Schedule;
import model.DeviceGroup;

import java.util.List;

public interface IScheduleView {

    void updateExistedGroup(Object[] arr);

    DeviceGroup getDeviceGroup();

    void showSchedule(List<Schedule> scheduleList);
}
