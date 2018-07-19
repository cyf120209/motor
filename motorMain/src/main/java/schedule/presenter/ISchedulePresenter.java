package schedule.presenter;

import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import model.DeviceGroup;
import view.Schedule;

import java.util.List;

public interface ISchedulePresenter {

    long insert(Schedule schedule);

    int update(int index,Schedule schedule);

    int delete(List<Integer> idList);

    int delete(int index);

    int deleteAll();
}
