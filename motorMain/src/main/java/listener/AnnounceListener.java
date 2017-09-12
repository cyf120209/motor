package listener;

import GroupOperation.presenter.GroupOperationPresenter;
import GroupOperation.view.GroupOperationView;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import util.DraperSubItem;
import util.DraperSubList;

import java.util.*;

/**
 * Created by lenovo on 2017/1/19.
 */
public class AnnounceListener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private GroupOperationPresenter mGroupOperationPresenter;
    GroupOperationView mGroupOperationView;

    public AnnounceListener(LocalDevice localDevice , GroupOperationPresenter mGroupOperationPresenter, GroupOperationView mGroupOperationView) {
        this.localDevice = localDevice;
        this.mGroupOperationPresenter=mGroupOperationPresenter;
        this.mGroupOperationView=mGroupOperationView;
    }

    @Override
    public void iHaveReceived(final RemoteDevice d, final RemoteObject o) {
    }

    @Override
    public void iAmReceived(final RemoteDevice d){

    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        mGroupOperationPresenter.paraseServiceParameter(serviceNumber, (Sequence) serviceParameters);
    }
}