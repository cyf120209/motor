package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import main.presenter.BoxLayoutCasePresenter;
import main.view.BoxLayoutView;
import util.MyLocalDevice;

import java.io.File;

/**
 * Created by lenovo on 2017/1/19.
 */
public class Listener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private File framefile;
    private BoxLayoutCasePresenter mBoxLayoutCasePresenter;
    private byte[] fileTmp;
    BoxLayoutView mBoxLayoutView;

    public Listener(BoxLayoutCasePresenter mBoxLayoutCasePresenter, BoxLayoutView mBoxLayoutView) {
        this.localDevice = MyLocalDevice.getInstance();
        this.mBoxLayoutCasePresenter=mBoxLayoutCasePresenter;
        this.mBoxLayoutView=mBoxLayoutView;
    }


    @Override
    public void iHaveReceived(final RemoteDevice d, final RemoteObject o) {

    }

    @Override
    public void iAmReceived(final RemoteDevice d){
        System.out.println("iHaveReceived"+d);
        boolean exist = MyLocalDevice.isExist(d);
        if(exist){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    MyLocalDevice.getObjectList(d);
                    mBoxLayoutView.AddItem(d);
                    MyLocalDevice.addRemoteDevice(d);
                } catch (BACnetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String modelName="";
//                try {
//                    System.out.println(localDevice);
//                    System.out.println(d);
//                    ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(d, new ReadPropertyRequest(d.getObjectIdentifier(), PropertyIdentifier.modelName));
//                    modelName=ack.getValue().toString();
//                }catch (NullPointerException e){
//                    System.out.println(e);
//                    e.printStackTrace();
//                } catch (BACnetException e) {
//                    e.printStackTrace();
//                }
//                d.setModelName(modelName);
//                mBoxLayoutView.AddItem(d);
//                MyLocalDevice.addRemoteDevice(d);
//            }
//        }).start();
    }


}