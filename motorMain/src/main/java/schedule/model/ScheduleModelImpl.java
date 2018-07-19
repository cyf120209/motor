package schedule.model;

import dao.ScheduleDao;
import entity.Schedule;
import entity.ScheduleGroupRelation;
import model.DeviceGroup;

import java.util.List;

public class ScheduleModelImpl implements IScheduleModel {

    private ScheduleDao scheduleDao;

    public ScheduleModelImpl() {
        scheduleDao = new ScheduleDao();
    }

    @Override
    public long insert(Schedule schedule) {
        return scheduleDao.insert(schedule);
    }

    @Override
    public int update(Schedule schedule) {
        return scheduleDao.update(schedule);

    }

    @Override
    public int delete(int id) {
        return scheduleDao.delete(id);
    }

    @Override
    public int delete(List<ScheduleGroupRelation> relationList) {
        int num=0;
        for (ScheduleGroupRelation relation:relationList){
            int delete = scheduleDao.deleteRelation(relation);
            num+=delete;
        }
        return num;
    }

    @Override
    public int deleteAll() {
        return scheduleDao.deleteAll();
    }


}
