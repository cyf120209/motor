package schedule.model;

import entity.Schedule;
import entity.ScheduleGroupRelation;

import java.util.List;

public interface IScheduleRelationModel {

    int addScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList);

    int saveScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList);

    int delScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList);

    Schedule getGroupListByScheduleId(int scheduleId);
}
