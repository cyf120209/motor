package schedule.presenter;

import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import entity.Schedule;
import model.DeviceGroup;

import java.util.List;

public interface IScheduleRelationPresenter {

    void paraseServiceParameter(UnsignedInteger serviceNumber, Sequence serviceParameters);

    void sendAnnounce();

    /**
     *
     * @param scheduleId scheduleId 行索引
     * @param addGroupList
     * @param delGroupList
     * @return
     */
    int saveScheduleGroup(int scheduleId,List<DeviceGroup> addGroupList,List<DeviceGroup> delGroupList);

    Schedule getGroupListByScheduleId(int scheduleId);
}
