package util;

import com.pi4j.io.spi.SpiDevice;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.Frame;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.npdu.uart.Spi2Uart;
import com.serotonin.bacnet4j.npdu.uart.UART;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.RequestUtils;
import common.Common;
import org.free.bacnet4j.util.SerialParameters;
import org.free.bacnet4j.util.SerialPortException;
import rx.*;

import javax.swing.*;
import java.util.*;

/**
 * Created by lenovo on 2017/2/5.
 */
public class MyLocalDevice {

    public static SerialParameters serialParams=new SerialParameters();
    private static LocalDevice localDevice;

    public static IpNetwork ipnetwork;
    public static Transport iptransport;
    public static MstpNetwork network;

//    /**
//     * 远程设备列表
//     */
//    private static List<RemoteDevice> mRemoteDeviceList=new ArrayList<>();
//
//    /**
//     * 远程设备列表
//     */
//    private static Map<Integer, RemoteDevice> mRemoteDevice = new HashMap<>();

    private static MasterNode node;
    private static Transport transport;
//    private static LocalDevice ipLocalDevice;
    public static RemoteUtils mRemoteUtils;

    public static LocalDevice getInstance(){
        return localDevice;
    }

    public static LocalDevice getInstance(String prot) throws Exception {
        if(localDevice==null){
            synchronized (LocalDevice.class){
                if(localDevice==null){
                    //串口
//                    serialParams.setCommPortId(prot);
//                    serialParams.setBaudRate(Common.BAUDRATE);
//                    node = new MasterNode(serialParams, (byte) 2,2);
//                    network = new MstpNetwork(node);
//                    transport = new Transport(network);

                    //IP
//                    IpNetwork network = new IpNetwork("192.168.20.63");
//                    Transport transport = new Transport(network);
//                    transport.setTimeout(15000);
//                    transport.setSegTimeout(15000);

                    //UART
                    Spi2Uart spi2Uart = new Spi2Uart(SpiDevice.DEFAULT_SPI_SPEED_100k, UART.BAUDRATE_38400);
                    node=new MasterNode(spi2Uart.getUartInputStream(),spi2Uart.getUartOutputStream(),(byte) 2,2);
                    network = new MstpNetwork(node);
                    transport = new Transport(network);

                    localDevice = new LocalDevice(900, 900900, transport);

                    ////路由功能
//                    ipnetwork=new IpNetwork();
//                    iptransport = new Transport(ipnetwork);
//                    ipLocalDevice = new LocalDevice(901, 900912, iptransport);
//                    ipnetwork.peerNet=network;
//                    network.peerNet=ipnetwork;
//                    network.enableRouter=true;
//                    ipnetwork.enableRouter=true;
                    mRemoteUtils = new RemoteUtils();
                    init();
                }
            }
        }
        return localDevice;
    }

    private static void init() throws Exception {
        //固件升级失败后回调
        List<SequenceDefinition.ElementSpecification> elements2 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_PRODUCT_MODE_NAME, CharacterString.class, false, false));
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_START_OFFSET, UnsignedInteger.class, false, false));
        elements2.add(new SequenceDefinition.ElementSpecification(Draper.GET_FRIMEBLOCK_BLOCK_SZIE, UnsignedInteger.class, false, false));
        SequenceDefinition def2 = new SequenceDefinition(elements2);
        ConfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(Draper.GETFRAME_CONF_SERSUM)),def2);
        //电机，设备，组的关系
        List<SequenceDefinition.ElementSpecification> elements3 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements3.add(new SequenceDefinition.ElementSpecification("draperID", ObjectIdentifier.class, false, false));
        elements3.add(new SequenceDefinition.ElementSpecification("Motor Number", UnsignedInteger.class, false, false));
        elements3.add(new SequenceDefinition.ElementSpecification("ShadeGroup", DraperSubList.class, false, false));
        SequenceDefinition def3 = new SequenceDefinition(elements3);
        UnconfirmedPrivateTransferRequest.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(7)), def3);
        //固件升级
        List<SequenceDefinition.ElementSpecification> elements4 = new ArrayList<SequenceDefinition.ElementSpecification>();
        elements4.add(new SequenceDefinition.ElementSpecification("DraperInformation", DraperInformationItem.class, false, false));
        SequenceDefinition def4 = new SequenceDefinition(elements4);
        ConfirmedPrivateTransferAck.vendorServiceResolutions.put(new VendorServiceKey(new UnsignedInteger(900),
                new UnsignedInteger(8)), def4);


        //路由功能
//        LinkedList<Integer> myNet=new LinkedList<>();
//        myNet.add(1);
//        myNet.add(2001);
        try {
            localDevice.initialize();
            Thread.sleep(1000);
            localDevice.sendGlobalBroadcast(localDevice.getIAm());
//            Thread.sleep(100);
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
        } catch (BACnetException e) {
            e.printStackTrace();
            if (localDevice != null) {
                localDevice.terminate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (localDevice != null) {
                localDevice.terminate();
            }
        }
    }

    public static void stop(){
        if (localDevice != null) {
            localDevice.terminate();
            localDevice = null;
        }

    }

    /**
     * 判断设备是否已添加
     * @param remoteDevice
     * @return
     */
    public static boolean isExist(RemoteDevice remoteDevice){
        return mRemoteUtils.isExist(remoteDevice);
//        return mRemoteDeviceList.contains(remoteDevice);
    }

    /**
     * 添加远程设备
     * @param remoteDevice
     */
    public static void addRemoteDevice(RemoteDevice remoteDevice) throws BACnetException {
        //getObjectList(remoteDevice);
        mRemoteUtils.addRemoteDevice(remoteDevice);

//        mRemoteDeviceList.add(remoteDevice);
//        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
    }

    /**
     * 添加远程设备
     * @param remoteDevice
     */
    public static void updateRemoteDevice(RemoteDevice remoteDevice) throws BACnetException {
        //getObjectList(remoteDevice);
        mRemoteUtils.updateRemoteDevice(remoteDevice);

//        mRemoteDeviceList.add(remoteDevice);
//        mRemoteDevice.put(remoteDevice.getInstanceNumber(),remoteDevice);
    }

    /**
     * 清空 mRemoteDeviceList,mRemoteDevice
     */
    public static void clearRemoteDevice(){
        mRemoteUtils.clearRemoteDevice();
//        mRemoteDeviceList.clear();
//        mRemoteDevice.clear();
    }

    /**
     * 获取远程设备列表
     * @return
     */
    public static List<RemoteDevice> getRemoteDeviceList() {
        if(mRemoteUtils==null){
            return new ArrayList<>();
        }
        return mRemoteUtils.getRemoteDeviceList();
//        return mRemoteDeviceList;
    }

    /**
     * 获取deviceID，device的关系列表
     * @return
     */
    public static Map<Integer, RemoteDevice> getRemoteDeviceMap() {
        return mRemoteUtils.getRemoteDeviceMap();
//        return mRemoteDevice;
    }

    /**
     * 获取设备，组，电机的关系
     * @return
     */
    public static Map<Integer, Map<Integer, List<Integer>>> getRelationMap() {
        if(mRemoteUtils==null){
            return new HashMap<>();
        }else {
            return mRemoteUtils.getRelationMap();
        }
    }

    /**
     * 设置设备，组，电机的关系
     * @param map
     */
    public static void setRelationMap(Map<Integer, Map<Integer, List<Integer>>> map) {
        mRemoteUtils.setRelationMap(map);
    }

    /**
     * 获取升级进度
     * @return
     */
    public static List<Frame> getFrameToSend(){
        return node.getFramesToSend();
    }

    public static List<Byte> getAddressList(){
        return node.getAddressList();
    }

    /**
     * 获取设备的属性，如：modelName，firmwireversion
     * @param d
     * @throws BACnetException
     */
    @SuppressWarnings("unchecked")
    public static void getObjectList(RemoteDevice d) throws BACnetException {
        //LOG.out("Getting extended information");
        RequestUtils.getExtendedDeviceInformation(localDevice, d);
        // LOG.out("Got extended information");
        /*
        // Get the device's object list.
        // LOG.out("Getting object list");
        java.util.List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
        // LOG.out("Got object list: " + oids.size());

        PropertyReferences refs = new PropertyReferences();
        for (ObjectIdentifier oid : oids)
            addPropertyReferences(refs, oid);

        // LOG.out("Getting properties: " + refs.size());
        RequestUtils.readProperties(localDevice, d, refs, new RequestListener() {
            @Override
            public boolean requestProgress(double d, ObjectIdentifier oid, PropertyIdentifier pid,
                                           UnsignedInteger unsignedinteger, Encodable encodable) {
                return false;
            }
        });*/
        // LOG.out("Got properties");
    }

    public static void addPropertyReferences(PropertyReferences refs, ObjectIdentifier oid) {
        refs.add(oid, PropertyIdentifier.objectName);

        ObjectType type = oid.getObjectType();
        if (ObjectType.accumulator.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        } else if (ObjectType.analogInput.equals(type) || ObjectType.analogOutput.equals(type)
                || ObjectType.analogValue.equals(type) || ObjectType.pulseConverter.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        } else if (ObjectType.binaryInput.equals(type) || ObjectType.binaryOutput.equals(type)
                || ObjectType.binaryValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.inactiveText);
            refs.add(oid, PropertyIdentifier.activeText);
            refs.add(oid, PropertyIdentifier.presentValue);
        } else if (ObjectType.device.equals(type)) {
            refs.add(oid, PropertyIdentifier.modelName);
        } else if (ObjectType.lifeSafetyPoint.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        } else if (ObjectType.loop.equals(type)) {
            refs.add(oid, PropertyIdentifier.outputUnits);
            refs.add(oid, PropertyIdentifier.presentValue);
        } else if (ObjectType.multiStateInput.equals(type) || ObjectType.multiStateOutput.equals(type)
                || ObjectType.multiStateValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.stateText);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
    }
}
