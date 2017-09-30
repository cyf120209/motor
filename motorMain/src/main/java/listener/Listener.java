package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import main.presenter.BoxLayoutCasePresenter;
import main.view.BoxLayoutView;
import util.MyLocalDevice;
import util.Public;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

/**
 * Created by lenovo on 2017/1/19.
 */
public class Listener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    private File framefile;
    private BoxLayoutCasePresenter mBoxLayoutCasePresenter;
    private byte[] fileTmp;
    BoxLayoutView mBoxLayoutView;

    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();


    private Timer timer = new Timer(5 * 1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
//            List<Byte> addressList = MyLocalDevice.getAddressList();
//            if(addressList.size()!=remoteDeviceList.size()){
//                for(RemoteDevice remoteDevice:remoteDeviceList){
//                    OctetString macAddress = remoteDevice.getAddress().getMacAddress();
//                    if(!addressList.contains(macAddress.getMstpAddress())){
//                        remoteDeviceList.remove(remoteDevice);
//                    }
//                }
//            }
        }
    });

    public Listener(BoxLayoutCasePresenter mBoxLayoutCasePresenter, BoxLayoutView mBoxLayoutView) {
        this.localDevice = MyLocalDevice.getInstance();
        this.mBoxLayoutCasePresenter=mBoxLayoutCasePresenter;
        this.mBoxLayoutView=mBoxLayoutView;
        timer.start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    MyLocalDevice.getObjectList(d);
                    MyLocalDevice.addRemoteDevice(d);
                    if(Public.matchString(d.getModelName(),"MC-AC")){
                        mBoxLayoutView.AddItem(d);
                    }
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