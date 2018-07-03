package mapper;

import entity.Log;
import entity.Schedule;

import java.util.List;

public interface ScheduleMapper {

    List<Schedule> queryAll();

    Schedule selectByScheduleId(int scheduleId);

    void insert(Schedule schedule);

    void update(Schedule schedule);

    void delete(int scheduleId);

    void deleteAll();
}
