package main.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.MstpNode;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.*;
import listener.Listener;
import org.free.bacnet4j.util.SerialPortException;
import util.MyLocalDevice;
import util.Draper;
import main.view.BoxLayoutView;

import javax.swing.*;

/**
 * Created by lenovo on 2017/1/19.
 */
public class BoxLayoutCasePresenterImpl implements BoxLayoutCasePresenter {

    private BoxLayoutView mBoxLayoutView;

    private boolean running = false;
    public static LocalDevice localDevice;


    public BoxLayoutCasePresenterImpl(BoxLayoutView mBoxLayoutView) {
        this.mBoxLayoutView = mBoxLayoutView;
    }


    @Override
    public void startstopbt() {
        if (running) {
            BACnetStop();
            mBoxLayoutView.updatedevBox("start", true);
            running = false;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BACnetStart(mBoxLayoutView.getComBoxSelectedItem());
                    } catch (SerialPortException e){
                        JOptionPane.showMessageDialog(null, "The serial port is occupied! ", "alert", JOptionPane.WARNING_MESSAGE);
                        BACnetStop();
                        mBoxLayoutView.updatedevBox("start", true);
                        running = false;
//                        if (localDevice != null) {
//                            localDevice.terminate();
//                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
            mBoxLayoutView.updatedevBox("stop", false);
            running = true;
        }
    }

    private void BACnetStart(String port) throws Exception {
        MstpNode.DEBUG = false;
        localDevice = MyLocalDevice.getInstance(port);
//        java.util.List<SequenceDefinition.ElementSpecification> elements2 = new ArrayList<SequenceDefinition.ElementSpecification>();
//        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_PRODUCT_MODE_NAME, CharacterString.class, false, false));
//        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_START_OFFSET, UnsignedInteger.class, false, false));
//        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_BLOCK_SZIE, UnsignedInteger.class, false, false));
//        SequenceDefinition def2 = new SequenceDefinition(elements2);
//        ConfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
//                new UnsignedInteger(Draper.GETFRAME_CONF_SERSUM)), def2);

//        java.util.List<SequenceDefinition.ElementSpecification> elements3 = new ArrayList<SequenceDefinition.ElementSpecification>();

//        elements3.add(new SequenceDefinition.ElementSpecification("draperID", ObjectIdentifier.class, false, false));
//        elements3.add(new SequenceDefinition.ElementSpecification("Motor Number", UnsignedInteger.class, false, false));
//        elements3.add(new SequenceDefinition.ElementSpecification("ShadeGroup", DraperSubList.class, false, false));
//        SequenceDefinition def3 = new SequenceDefinition(elements3);
//        UnconfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
//                new UnsignedInteger(7)), def3);

//        java.util.List<SequenceDefinition.ElementSpecification> elements4 = new ArrayList<SequenceDefinition.ElementSpecification>();
//        elements4.add(new SequenceDefinition.ElementSpecification("DraperInformation", DraperInformationItem.class, false, false));
//        SequenceDefinition def4 = new SequenceDefinition(elements4);
//        ConfirmedPrivateTransferAck.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
//                new UnsignedInteger(8)), def4);

        localDevice.getEventHandler().addListener(new Listener(this, mBoxLayoutView));
        try {
//            localDevice.initialize();
//            Thread.sleep(1000);
//            localDevice.sendGlobalBroadcast(localDevice.getIAm());
//            Thread.sleep(1000);
//            localDevice.sendGlobalBroadcast(new WhoHasRequest(new WhoHasRequest.Limits(new UnsignedInteger(0), new UnsignedInteger(4194303)), new ObjectIdentifier(ObjectType.analogOutput, 0)));
            Thread.sleep(100);
//            localDevice.sendGlobalBroadcast(new WhoHasRequest(new WhoHasRequest.Limits(new UnsignedInteger(0), new UnsignedInteger(4194303)), new ObjectIdentifier(ObjectType.analogOutput, 1)));
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            while (running && localDevice != null) {
                Thread.sleep(300);
            }
        } finally {
//            if (localDevice != null)
//                localDevice.terminate();
            MyLocalDevice.stop();
        }
    }

    @Override
    public void upBt() {

        if (localDevice != null) {
            try {
                Draper.sendCmd(Draper.DRAPER_CMD_RETRACTED);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void stopButton() {
        if (localDevice != null) {
            try {
                Draper.sendCmd( Draper.DRAPER_CMD_STOP);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void downbt() {
        if (localDevice != null) {
            try {
                Draper.sendCmd( Draper.DRAPER_CMD_EXTENDED);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void cmdTestbt() {
        try {
            Draper.sendCmd( Integer.parseInt(mBoxLayoutView.getCmdTextEdit()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void cmdTestOneBT() {
        try {
            Draper.sendCmd( mBoxLayoutView.getdevBoxSelectedItem().getAddress(), Integer.parseInt(mBoxLayoutView.getCmdTextEdit()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void ReadVersion() {
        try {
            mBoxLayoutView.updateVersionLabel("Version: NULL");
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(mBoxLayoutView.getdevBoxSelectedItem(), new ReadPropertyRequest(mBoxLayoutView.getdevBoxSelectedItem().getObjectIdentifier(), PropertyIdentifier.firmwareRevision));
            mBoxLayoutView.updateVersionLabel("Version:" + ack.getValue().toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void ReadValue() {
        try {
            localDevice.send(mBoxLayoutView.getdevBoxSelectedItem(), new ReadPropertyRequest(new ObjectIdentifier(ObjectType.analogOutput, 1), PropertyIdentifier.presentValue));
        } catch (BACnetException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void testFunc() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (RemoteDevice r : localDevice.getRemoteDevices()) {
                        try {
                            Draper.sendCmd( r.getAddress(), 3);
                            Thread.sleep(10000);
                            Draper.sendCmd( r.getAddress(), 1);
                            Thread.sleep(1000);
                            Draper.sendCmd( r.getAddress(), 4);
                            Thread.sleep(10000);
                            Draper.sendCmd( r.getAddress(), 1);
                            Thread.sleep(1000);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (localDevice != null) {
                        break;
                    }
                }
            }
        }).start();

    }

    @Override
    public void whohasTest() {
        if (localDevice != null) {
            try {
//                localDevice.sendGlobalBroadcast(new WhoHasRequest(new WhoHasRequest.Limits(new UnsignedInteger(0), new UnsignedInteger(4194303)), new ObjectIdentifier(ObjectType.analogOutput, 1)));
                Draper.sendAnnounceDraperInformation(mBoxLayoutView.getdevBoxSelectedItem());
            } catch (BACnetException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void BACnetStop() {
        MyLocalDevice.stop();
//        if (localDevice != null) {
//            localDevice.terminate();
//            localDevice = null;
//        }
    }

    @Override
    public boolean getRunningState() {
        return running;
    }

    //    @SuppressWarnings("unchecked")
//    void getObjectList(RemoteDevice d) throws BACnetException {
//        //LOG.out("Getting extended information");
//        RequestUtils.getExtendedDeviceInformation(localDevice, d);
//        // LOG.out("Got extended information");
//
//        // Get the device's object list.
//        // LOG.out("Getting object list");
//        java.util.List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
//                localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
//        // LOG.out("Got object list: " + oids.size());
//
//        PropertyReferences refs = new PropertyReferences();
//        for (ObjectIdentifier oid : oids)
//            addPropertyReferences(refs, oid);
//
//        // LOG.out("Getting properties: " + refs.size());
//        RequestUtils.readProperties(localDevice, d, refs, new RequestListener() {
//            @Override
//            public boolean requestProgress(double d, ObjectIdentifier oid, PropertyIdentifier pid,
//                                           UnsignedInteger unsignedinteger, Encodable encodable) {
//                return false;
//            }
//        });
//        // LOG.out("Got properties");
//    }
//
//    void addPropertyReferences(PropertyReferences refs, ObjectIdentifier oid) {
//        refs.add(oid, PropertyIdentifier.objectName);
//
//        ObjectType type = oid.getObjectType();
//        if (ObjectType.accumulator.equals(type)) {
//            refs.add(oid, PropertyIdentifier.units);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        } else if (ObjectType.analogInput.equals(type) || ObjectType.analogOutput.equals(type)
//                || ObjectType.analogValue.equals(type) || ObjectType.pulseConverter.equals(type)) {
//            refs.add(oid, PropertyIdentifier.units);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        } else if (ObjectType.binaryInput.equals(type) || ObjectType.binaryOutput.equals(type)
//                || ObjectType.binaryValue.equals(type)) {
//            refs.add(oid, PropertyIdentifier.inactiveText);
//            refs.add(oid, PropertyIdentifier.activeText);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        } else if (ObjectType.device.equals(type)) {
//            refs.add(oid, PropertyIdentifier.modelName);
//        } else if (ObjectType.lifeSafetyPoint.equals(type)) {
//            refs.add(oid, PropertyIdentifier.units);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        } else if (ObjectType.loop.equals(type)) {
//            refs.add(oid, PropertyIdentifier.outputUnits);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        } else if (ObjectType.multiStateInput.equals(type) || ObjectType.multiStateOutput.equals(type)
//                || ObjectType.multiStateValue.equals(type)) {
//            refs.add(oid, PropertyIdentifier.stateText);
//            refs.add(oid, PropertyIdentifier.presentValue);
//        }
//    }


}
