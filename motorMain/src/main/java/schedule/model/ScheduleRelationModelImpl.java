package schedule.model;

import dao.ScheduleDao;
import entity.Schedule;
import entity.ScheduleGroupRelation;

import java.util.List;

public class ScheduleRelationModelImpl implements IScheduleRelationModel{

    private ScheduleDao scheduleDao;

    public ScheduleRelationModelImpl() {
        scheduleDao = new ScheduleDao();
    }

    @Override
    public int addScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList) {
        return scheduleDao.insertRelation(scheduleGroupRelationList);
    }

    @Override
    public int saveScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList) {
        return 0;
    }

    @Override
    public int delScheduleGroup(List<ScheduleGroupRelation> scheduleGroupRelationList) {
        return scheduleDao.deleteRelation(scheduleGroupRelationList);
    }

    @Override
    public Schedule getGroupListByScheduleId(int scheduleId) {
        return scheduleDao.selectById(scheduleId);
    }
}
