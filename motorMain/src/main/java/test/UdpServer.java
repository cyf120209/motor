package test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.Byte2IntUtils;
import util.MyLocalDevice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static com.serotonin.bacnet4j.type.enumerated.ProgramState.running;

/**
 * Created by lenovo on 2017/5/5.
 */
public class UdpServer extends Thread{

    MstpNetwork network;

    public UdpServer(MstpNetwork network){
        this.network=network;
    }

    public UdpServer() {
    }

    public static void main(String[] args) {
        new UdpServer().run();
    }

    @Override
    public void run() {
        super.run();
//       IpNetwork network = new IpNetwork();
//        Transport transport = new Transport(network);
//        LocalDevice localDevice = new LocalDevice(900, 900900, transport);
//
//        try {
//            localDevice.initialize();
//            Thread.sleep(1000);
//            localDevice.sendGlobalBroadcast(localDevice.getIAm());
//            Thread.sleep(1000);
//            localDevice.sendGlobalBroadcast(new WhoHasRequest(new WhoHasRequest.Limits(new UnsignedInteger(0),new UnsignedInteger(4194303)),new ObjectIdentifier(ObjectType.analogOutput,0)));
//            Thread.sleep(1000);
//            localDevice.sendGlobalBroadcast(new WhoHasRequest(new WhoHasRequest.Limits(new UnsignedInteger(0),new UnsignedInteger(4194303)),new ObjectIdentifier(ObjectType.analogOutput,1)));
//            localDevice.sendGlobalBroadcast(new WhoIsRequest());
//            while (true) {
//                Thread.sleep(300);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (BACnetException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(localDevice!=null)
//                localDevice.terminate();
//        }

        //监听9876端口
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(6000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[480];
        byte[] sendData = new byte[1024];
//        while(true) {
            //构造数据包接收数据
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            //接收数据
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //解析数据
            byte[] data = receivePacket.getData();
            String s = Byte2IntUtils.bytesToHexString(data);

            System.out.println("sendPacket---" + s);
//            network.sendData(data);

//            String sentence = new String( receivePacket.getData());
//            System.out.println("RECEIVED: " + sentence);
//        }
    }

}
