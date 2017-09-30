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
import update.presenter.UpdatePresenterImpl;
import util.MyLocalDevice;
import update.presenter.UpdatePresenter;
import update.view.UpdateView;
import util.Public;
import util.STExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/19.
 */
public class UpdateListener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private UpdatePresenter mUpdatePresenter;
    UpdateView mUpdateView;
    boolean isFirst=true;
    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();

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
        Integer id = Integer.valueOf(d.getInstanceNumber());
        boolean exist = remoteDeviceIDList.contains(id);
        if(exist){
            return;
        }
        remoteDeviceIDList.add(id);
        if(listener!=null && mUpdatePresenter.getFlag()!=3){
            if(remoteDeviceIDList.size()==MyLocalDevice.getAddressList().size() && mUpdatePresenter.getFlag()==1){
                listener.received();
//                System.out.println("-----+++++--------*****-----///////  origin 找到所有电机");
                mUpdateView.showUpgradeInformation("-----+++++--------*****-----///////  origin 找到所有电机");
            }else  if(remoteDeviceIDList.size()==mUpdateView.getOriginalSize()  && mUpdatePresenter.getFlag()==2){
                listener.received();
//                System.out.println("-----+++++--------*****-----///////  before 找到所有电机");
                mUpdateView.showUpgradeInformation("-----+++++--------*****-----///////  before 找到所有电机");
            }
        }


        //若flag为升级后 的状态，则更新Mylocaldevice
        if(mUpdatePresenter.getFlag()==3){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z-]");
                        //Public.matchStr(mUpdatePresenter.getFirmWareType(),"[A-Z](2)+");
                        if(Public.matchString(Public.readModelName(d),reg)) {
                            mUpdateView.updateDevBox(d);
                            MyLocalDevice.updateRemoteDevice(d);
                            mUpdatePresenter.addJListDevice(d);
                        }
                    } catch (BACnetException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
//            STExecutor.submit(runnable);
        }else if(!UpdatePresenterImpl.isSingle && mUpdatePresenter.getFlag()!=3){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z-]");
                    //Public.matchStr(mUpdatePresenter.getFirmWareType(),"[A-Z](2)+");
                    if(Public.matchString(Public.readModelName(d),reg)) {
                        mUpdatePresenter.addJListDevice(d);
                    }
                }
            };
            new Thread(runnable).start();
//            STExecutor.submit(runnable);
        }else if(UpdatePresenterImpl.isSingle && mUpdatePresenter.getFlag()!=3){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String reg = Public.getAllString(mUpdatePresenter.getFirmWareType(), "[A-za-z-]");
                    if(mUpdateView.getdevBoxSelectedItem().equals(d)){
                        if(Public.matchString(Public.readModelName(d),reg)) {
                            mUpdatePresenter.addJListDevice(d);
                        }
//                        if(listener!=null){
//                            listener.received();
//                        }
                    }
                }
            };
            new Thread(runnable).start();
//            STExecutor.submit(runnable);

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

    public void clearRemoteDeviceList() {
        this.remoteDeviceIDList.clear();
    }


    ReceivedListener listener;

    public void setListener(ReceivedListener listener) {
        this.listener = listener;
    }

    public interface ReceivedListener{
        void received();
    }
}