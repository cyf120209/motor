package manager.impl;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import util.MyLocalDevice;

import java.util.LinkedList;

public class RouterManager {

    private static IpNetwork ipnetwork;
    private static MstpNetwork  network ;
    private static LocalDevice ipLocalDevice;
    private static final String OTHER_BROADCAST_IP = "192.168.20.255";
    private static final int OTHER_PORT = 0xBABF; // == 47807

    public static void register() {
        ipnetwork = new IpNetwork(OTHER_BROADCAST_IP,OTHER_PORT);
        Transport iptransport = new Transport(ipnetwork);
        ipLocalDevice = new LocalDevice(901, 900912, iptransport);
        network = MyLocalDevice.network;
        ipnetwork.peerNet = network;
        network.peerNet = ipnetwork;
        network.enableRouter = true;
        ipnetwork.enableRouter = true;
        LinkedList<Integer> myNet = new LinkedList<>();
        myNet.add(1);
        myNet.add(2001);
        try {
            ipLocalDevice.initialize();
            Thread.sleep(500);
            network.SendIamRouter(myNet);
            ipnetwork.SendIamRouter(myNet);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister(){
        ipnetwork.peerNet = null;
        network.peerNet = null;
        network.enableRouter = false;
        ipnetwork.enableRouter = false;
        stop();
    }

    public static void stop() {
        if (ipLocalDevice != null) {
            ipLocalDevice.terminate();
            ipLocalDevice = null;
        }
    }
}
