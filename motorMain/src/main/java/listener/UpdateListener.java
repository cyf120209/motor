package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import util.MyLocalDevice;
import update.presenter.UpdatePresenter;
import update.view.UpdateView;
import util.STExecutor;

/**
 * Created by lenovo on 2017/1/19.
 */
public class UpdateListener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private UpdatePresenter mUpdatePresenter;
    UpdateView mUpdateView;
    boolean isFirst=true;

    public UpdateListener(LocalDevice localDevice , UpdatePresenter mUpdatePresenter, UpdateView updateView) {
        this.localDevice = localDevice;
        this.mUpdatePresenter=mUpdatePresenter;
        this.mUpdateView=updateView;
    }

    @Override
    public void iHaveReceived(final RemoteDevice d, final RemoteObject o) {
    }


    @Override
    public void iAmReceived(final RemoteDevice d){
        //若flag为升级后 的状态，则更新Mylocaldevice
        if(mUpdatePresenter.getFlag()==3){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        MyLocalDevice.clearRemoteDevice();
                        MyLocalDevice.addRemoteDevice(d);
                        mUpdateView.updateDevBox(d);
                        mUpdatePresenter.addJListDevice(d);
                    } catch (BACnetException e) {
                        e.printStackTrace();
                    }
                }
            };
            STExecutor.submit(runnable);
        }else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mUpdatePresenter.addJListDevice(d);
                }
            };
            STExecutor.submit(runnable);

        }
    }

    @Override
    public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters) {
        //super.privateTransferReceived(vendorId, serviceNumber, serviceParameters);
        System.out.println("111");
        Sequence parms = (Sequence)serviceParameters;
        if(serviceNumber.intValue()==7){
            for (int i=0;i<2;i++){
                    String RC = parms.getValues().get("Device RC"+i).toString();
                    String GP = parms.getValues().get("Device GP"+i).toString();
                    System.out.println(RC+"----"+GP);
            }
        }
    }

    @Override
    public AcknowledgementService privateTransferReceivedComplex(UnsignedInteger vendorId, UnsignedInteger serviceNumber, Encodable serviceParameters, Address address) {
        return mUpdatePresenter.privateTransferReceivedComplex(vendorId,serviceNumber,serviceParameters,address);
    }

}