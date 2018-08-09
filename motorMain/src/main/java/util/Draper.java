package util;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.*;
import entity.DraperInformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class Draper {
    public static final int DRAPER_CMD_STOP=1;
    public static final int DRAPER_CMD_RETRACTED=3;
    public static final int DRAPER_CMD_EXTENDED=4;
    public static final int DRAPER_VENDOR_ID=900;
    public static final int CMD_UNCONF_SERNUM=1;
    public static final int HAVEFRAME_UNCONF_SERNUM=2;
    public static final int GETFRAME_CONF_SERSUM=3;
    public static final int FRAME_BLOCK_UNCONF_SEERNUM=4;
    public static final int SUBSCRIPTION_GROUP=5;
    public static final int ANNOUNCE_GROUP=6;
    public static final int ANNOUNCE_DRAPER_INFORMATION=8;
    public static final int DRAPER_CONFIGURATION=9;

    public static final String GET_FRIMEBLOCK_PRODUCT_MODE_NAME="Product Model Name";
    public static final String GET_FRIMEBLOCK_START_OFFSET="Start Offset";
    public static final String GET_FRIMEBLOCK_BLOCK_SZIE="Block Size";

    public static final UnsignedInteger cmdSer=new UnsignedInteger(CMD_UNCONF_SERNUM);
    public static UnsignedInteger vendorID=new UnsignedInteger(DRAPER_VENDOR_ID);
    public static UnsignedInteger haveFrame=new UnsignedInteger(HAVEFRAME_UNCONF_SERNUM);
    public static UnsignedInteger brocastFram=new UnsignedInteger(FRAME_BLOCK_UNCONF_SEERNUM);
    public static UnsignedInteger subscription=new UnsignedInteger(SUBSCRIPTION_GROUP);
    public static UnsignedInteger announce=new UnsignedInteger(ANNOUNCE_GROUP);
    public static UnsignedInteger announceDraperInformation=new UnsignedInteger(ANNOUNCE_DRAPER_INFORMATION);
    public static ObjectIdentifier deviceid=new ObjectIdentifier(ObjectType.device,900900);
    public static final UnsignedInteger draperConfiguration=new UnsignedInteger(DRAPER_CONFIGURATION);
    public static final UnsignedInteger cmdAbsoluteSer=new UnsignedInteger(11);

    private static LocalDevice dev;

    public static void updateLocalDevice(){
        dev= MyLocalDevice.getInstance();
    }
    
    private static int calcDataCRC(int dataValue, int crcValue) {
        int crcLow = (crcValue & 0xff) ^ dataValue; /* XOR C7..C0 with D7..D0 */
        /* Exclusive OR the terms in the table (top down) */
        int crc = (crcValue >> 8) ^ (crcLow << 8) ^ (crcLow << 3) ^ (crcLow << 12) ^ (crcLow >> 4) ^ (crcLow & 0x0f)
                ^ ((crcLow & 0x0f) << 7);
        return crc & 0xffff;
    }

    public static void  sendIHaveFrameToOne(String _type, RemoteDevice peer, int major, int minor, int patch, int type, File file) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fi = new FileInputStream(file);
        int crc =0;
        listParam.add(new CharacterString(_type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new Enumerated(type));
        listParam.add(new UnsignedInteger(file.length()));
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }else {
            for (byte tmp : buffer)
                crc = calcDataCRC(tmp & 0xff, crc);
        }
        fi.close();
        Unsigned16 pramCrc=new Unsigned16(crc);
        listParam.add(pramCrc);
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,haveFrame,listParam));
    }
    public static void sendFrameWareToOne( String type,RemoteDevice peer, int major, int minor, int patch, int filesize, int startoffset, byte[] data) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        // listParam.add(deviceid);
        listParam.add(new CharacterString(type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new UnsignedInteger(filesize));
        listParam.add(new UnsignedInteger(startoffset));
        listParam.add(new OctetString(data));
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,brocastFram,listParam));
    }
    public static void  sendIHaveFrame( String _type,int major, int minor, int patch, int type, File file) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fi = new FileInputStream(file);
        int crc =0;
        listParam.add(new CharacterString(_type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new Enumerated(type));
        listParam.add(new UnsignedInteger(file.length()));
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }else {
            for (byte tmp : buffer)
                crc = calcDataCRC(tmp & 0xff, crc);
        }
        fi.close();
        Unsigned16 pramCrc=new Unsigned16(crc);
        listParam.add(pramCrc);
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,haveFrame,listParam));

    }

    public static void  sendIHaveFrame( String _type,int major, int minor, int patch, int type,byte[] buffer) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        int crc =0;
        listParam.add(new CharacterString(_type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new Enumerated(type));
        listParam.add(new UnsignedInteger(buffer.length));
        int offset = 0;
        int numRead = 0;
        for (byte tmp : buffer)
            crc = calcDataCRC(tmp & 0xff, crc);
        Unsigned16 pramCrc=new Unsigned16(crc);
        listParam.add(pramCrc);
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,haveFrame,listParam));

    }

    //6.1	SHADE COMMAND
    public static  void sendCmd( int cmd) throws Exception {
        sendCmd(cmd,7);
    }

    public static  void sendCmd( int cmd,int priority) throws Exception {
        sendCmd(cmd,priority,false);
    }

    public static  void sendCmd( int cmd,boolean absolute) throws Exception {
        sendCmd(cmd,7,absolute);
    }

    public static  void sendCmd( int cmd,int priority,boolean absolute) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(priority));
        if(absolute){
            dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,cmdAbsoluteSer,listParam));
        }else {
            dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID, cmdSer, listParam));
        }
    }

    //6.1	SHADE COMMAND
    public static  void sendCmd(RemoteDevice remoteDevice, int cmd) throws BACnetException {
        sendCmd(remoteDevice,cmd,7);
    }

    public static  void sendCmd(RemoteDevice remoteDevice, int cmd,int priority) throws BACnetException {
        sendCmd(remoteDevice,cmd,priority,1);
    }

    public static  void sendCmd(int cmd,int cmdService,RemoteDevice remoteDevice) throws BACnetException {
        sendCmd(remoteDevice,cmd,7,cmdService);
    }

    public static  void sendCmd(RemoteDevice remoteDevice, int cmd,int priority,int cmdService) throws BACnetException {
        sendCmd(remoteDevice,0,cmd,priority,cmdService);
    }

    /**
     *
     * @param remoteDevice
     * @param i 0：全部  （四合一1： 第一台电机 2：第二天电机 3：第三台电机 4：第四台电机）
     * @param cmd 命令
     * @param priority 优先级
     * @param cmdService 命令等级
     * @throws BACnetException
     */
    public static  void sendCmd(RemoteDevice remoteDevice, int i,int cmd,int priority,int cmdService) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(i));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(priority));
        dev.sendUnconfirmed(remoteDevice.getAddress(),remoteDevice.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,new UnsignedInteger(cmdService),listParam));
    }

    //6.1	SHADE COMMAND
    public static  void sendCmd( int deviceID,int groupID,int cmd) throws Exception {
        sendCmd(deviceID,groupID,cmd,7);
    }

    public static  void sendCmd( int deviceID,int groupID,int cmd,int priority) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(priority));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(groupID));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));

    }

    public static  void sendCmd( Address addr, int cmd) throws Exception {
        sendCmd(addr,cmd,7);
    }

    public static  void sendCmd( Address addr, int cmd,int priority) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(cmd));
        listParam.add(new Unsigned8(priority));
        dev.sendUnconfirmed(addr,new UnconfirmedPrivateTransferRequest(vendorID,cmdSer,listParam));

    }

    public static void sendFrameWare( String type,int major, int minor, int patch, int filesize, int startoffset, byte[] data) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
       // listParam.add(deviceid);
        listParam.add(new CharacterString(type));
        listParam.add(new Unsigned16(major));
        listParam.add(new Unsigned16(minor));
        listParam.add(new Unsigned16(patch));
        listParam.add(new UnsignedInteger(filesize));
        listParam.add(new UnsignedInteger(startoffset));
        listParam.add(new OctetString(data));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,brocastFram,listParam));
    }

    //6.2	GROUP SUBSCRIPTION
    public static void sendGroupSubscription( boolean remove, int deviceID, int GroupID) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(GroupID));
        listParam.add(new Boolean(remove));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,subscription,listParam));

    }
    public static void sendGroupSubscriptionToSelect( RemoteDevice peer, boolean remove, int deviceID, int GroupID) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        listParam.add(new Unsigned8(0));
        listParam.add(new ObjectIdentifier(ObjectType.device,deviceID));
        listParam.add(new Unsigned16(GroupID));
        listParam.add(new Boolean(remove));
        dev.sendUnconfirmed(peer.getAddress(),peer.getLinkService(),new UnconfirmedPrivateTransferRequest(vendorID,subscription,listParam));

    }

    public static void sendAnnounce() throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
    }

    public static void sendAnnounceDraperInformation(RemoteDevice peer) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
//        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
//        dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        AcknowledgementService send = dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        ConfirmedPrivateTransferAck result= (ConfirmedPrivateTransferAck) send;
        Sequence resultBlock = (Sequence)result.getResultBlock();
        Map<String, Encodable> values = resultBlock.getValues();
        DraperInformationItem encodable = (DraperInformationItem) values.get("DraperInformation");
        SignedInteger id = encodable.getId();
        Boolean isReverse = encodable.getIsReverse();
        SignedInteger curPos = encodable.getCurPos();
        SignedInteger upLimit = encodable.getUpLimit();
        SignedInteger lowLimit = encodable.getLowLimit();
        List<SignedInteger> presetStopList = encodable.getPersetStop();
        List<Integer> list=new ArrayList<>();
        for (SignedInteger signedInteger:presetStopList){
            list.add(new Integer(signedInteger.intValue()));
        }
        DraperInformation draperInformation = new DraperInformation(peer.getInstanceNumber(),isReverse.booleanValue(), curPos.intValue(), upLimit.intValue(), lowLimit.intValue(), list);
        RxBus.getDefault().post(draperInformation);
    }

    public static DraperInformation sendAnnounceDraperInformation(RemoteDevice peer, boolean b) throws BACnetException {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(deviceid);
//        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
//        dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        AcknowledgementService send = dev.send(peer,new ConfirmedPrivateTransferRequest(vendorID, announceDraperInformation, listParam));
        ConfirmedPrivateTransferAck result= (ConfirmedPrivateTransferAck) send;
        Sequence resultBlock = (Sequence)result.getResultBlock();
        Map<String, Encodable> values = resultBlock.getValues();
        DraperInformationItem encodable = (DraperInformationItem) values.get("DraperInformation");
        SignedInteger id = encodable.getId();
        Boolean isReverse = encodable.getIsReverse();
        SignedInteger curPos = encodable.getCurPos();
        SignedInteger upLimit = encodable.getUpLimit();
        SignedInteger lowLimit = encodable.getLowLimit();
        List<SignedInteger> presetStopList = encodable.getPersetStop();
        List<Integer> list=new ArrayList<>();
        for (SignedInteger signedInteger:presetStopList){
            list.add(new Integer(signedInteger.intValue()));
        }
        return new DraperInformation(peer.getInstanceNumber(),isReverse.booleanValue(), curPos.intValue(), upLimit.intValue(), lowLimit.intValue(), list);
    }

    /**
     * 配置
     * @param id
     * @param stance
     * @param step
     * @throws Exception
     */
    public static  void sendConfigure(int id,int stance,int step) throws Exception {
        SequenceOf<Primitive> listParam= new SequenceOf<Primitive>();
        listParam.add(new Unsigned8(0));
        listParam.add(new UnsignedInteger(stance));
//        listParam.add(new UnsignedInteger(step));
//        listParam.add(new Unsigned8(7));
        dev.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,draperConfiguration,listParam));
    }

}
