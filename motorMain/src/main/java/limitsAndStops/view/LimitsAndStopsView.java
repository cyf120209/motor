package limitsAndStops.view;

import com.serotonin.bacnet4j.RemoteDevice;

import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public interface LimitsAndStopsView {

    RemoteDevice getSelectedItem();

    void updateDevBox(List<RemoteDevice> remoteDevices);

    boolean getRunningState();

    int getDistance();
}
