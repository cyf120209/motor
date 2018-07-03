package suntracking.view;

import model.DeviceGroup;

import java.util.List;

public interface ISuntrackingView {

    void updateExistedGroup(Object[] arr);

    List<DeviceGroup> getDeviceGroup();
}
