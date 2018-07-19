package mapper;

import entity.Log;
import entity.Schedule;
import entity.ScheduleGroupRelation;

import java.util.List;

public interface ScheduleMapper {

    List<Schedule> queryAll();

    Schedule selectById(int scheduleId);

    long insert(Schedule schedule);

    int update(Schedule schedule);

    int delete(int scheduleId);

    int deleteAll();

    int deleteRelation(ScheduleGroupRelation relation);

    int insertRelation(List<ScheduleGroupRelation> scheduleGroupRelationList);

    int deleteRelation(List<ScheduleGroupRelation> scheduleGroupRelationList);
}
