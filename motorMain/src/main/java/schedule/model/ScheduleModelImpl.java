package schedule.model;

import dao.ScheduleDao;
import entity.Schedule;

import java.util.List;

public class ScheduleModelImpl implements IScheduleModel {

    private ScheduleDao scheduleDao;

    public ScheduleModelImpl() {
        scheduleDao = new ScheduleDao();
    }

    @Override
    public void insert(Schedule schedule) {
        scheduleDao.insert(schedule);
    }

    @Override
    public void update(Schedule schedule) {

    }

    @Override
    public void delete(List<Integer> idList) {
        for (Integer i:idList){
            scheduleDao.delete(i);
        }
    }

    @Override
    public void deleteAll() {
        scheduleDao.deleteAll();
    }
}
