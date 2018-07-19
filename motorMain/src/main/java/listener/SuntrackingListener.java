package listener;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import suntracking.presenter.SuntrackingPresenter;
import util.MyLocalDevice;
import util.Public;

import java.util.ArrayList;
import java.util.List;

public class SuntrackingListener extends DeviceEventAdapter {

    private SuntrackingPresenter mSuntrackingPresenter;
    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();

    public SuntrackingListener(SuntrackingPresenter suntrackingPresenter) {
        this.mSuntrackingPresenter=suntrackingPresenter;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        System.out.println("received");
        mSuntrackingPresenter.paraseServiceParameter(serviceNumber, (Sequence) serviceParameters);
    }
}
