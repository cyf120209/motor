package schedule.model;

import entity.Schedule;
import entity.ScheduleGroupRelation;
import model.DeviceGroup;

import java.util.List;

public interface IScheduleModel {

    long insert(Schedule schedule);

    int update(Schedule schedule);

    int delete(int id);

    int delete(List<ScheduleGroupRelation> relationList);

    int deleteAll();

}
