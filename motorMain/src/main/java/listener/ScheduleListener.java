package listener;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import schedule.presenter.ISchedulePresenter;
import schedule.presenter.IScheduleRelationPresenter;
import suntracking.presenter.SuntrackingPresenter;
import util.MyLocalDevice;
import util.Public;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListener extends DeviceEventAdapter {

    private IScheduleRelationPresenter mSchedulePresenter;
    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();

    public ScheduleListener(IScheduleRelationPresenter suntrackingPresenter) {
        this.mSchedulePresenter=suntrackingPresenter;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        System.out.println("received");
        mSchedulePresenter.paraseServiceParameter(serviceNumber, (Sequence) serviceParameters);
    }
}
