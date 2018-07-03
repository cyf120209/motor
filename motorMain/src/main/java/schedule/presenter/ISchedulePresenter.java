package schedule.presenter;

import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import model.DeviceGroup;
import view.Schedule;

import java.util.List;

public interface ISchedulePresenter {

    DeviceGroup getDeviceGroup();

    void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters);

    void sendAnnounce();

    void insert(Schedule schedule);

    void update(Schedule schedule);

    void delete(List<Integer> idList);

    void deleteAll();
}
