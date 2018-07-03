package schedule.model;

import entity.Schedule;

import java.util.List;

public interface IScheduleModel {

    void insert(Schedule schedule);

    void update(Schedule schedule);

    void delete(List<Integer> idList);

    void deleteAll();
}
