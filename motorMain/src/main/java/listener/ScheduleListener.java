package listener;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import schedule.presenter.ISchedulePresenter;
import suntracking.presenter.SuntrackingPresenter;
import util.MyLocalDevice;
import util.Public;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListener extends DeviceEventAdapter {

    private ISchedulePresenter mSchedulePresenter;
    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();

    public ScheduleListener(ISchedulePresenter suntrackingPresenter) {
        this.mSchedulePresenter=suntrackingPresenter;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
        Integer id = Integer.valueOf(d.getInstanceNumber());
        boolean exist = remoteDeviceIDList.contains(id);
        if (exist) {
            return;
        }
        remoteDeviceIDList.add(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyLocalDevice.addRemoteDevice(d);
                    if (Public.matchString(d.getModelName(), "MC-AC")) {
//                    mBoxLayoutView.AddItem(d);
                    }
                } catch (BACnetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        System.out.println("received");
        mSchedulePresenter.paraseServiceParameter(serviceNumber, (Sequence) serviceParameters);
    }
}
