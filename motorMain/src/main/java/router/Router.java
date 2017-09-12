package router;

import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import org.free.bacnet4j.util.SerialParameters;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/5/20.
 */
public class Router {
    public static int networkNUM=2001;
    public static SerialParameters serialParams=new SerialParameters();
    public static MstpNetwork mstpNetwork;
    public static Transport mstptransport;
    public static IpNetwork ipnetwork;
    public static Transport iptransport;
    public static void main(String[] args) {
        serialParams.setCommPortId("COM8");
        serialParams.setBaudRate(38400);
        MasterNode node = new MasterNode(serialParams, (byte) 0,2);
        mstpNetwork = new MstpNetwork(node,networkNUM);
        mstptransport = new Transport(mstpNetwork);
        ipnetwork=new IpNetwork();
        iptransport = new Transport(ipnetwork);
        ipnetwork.peerNet=mstpNetwork;
        mstpNetwork.peerNet=ipnetwork;
        mstpNetwork.enableRouter=true;
        mstpNetwork.enableRouter=true;
        LinkedList <Integer> myNet=new LinkedList<>();
        myNet.add(1);
        myNet.add(2001);
        try {
//            localDevice.initialize();
            node.initialize();
            iptransport.initialize();
            Thread.sleep(500);
            mstptransport.initialize();
            Thread.sleep(500);
            mstpNetwork.SendIamRouter(myNet);
            ipnetwork.SendIamRouter(myNet);
            //Address destination, Address source, boolean expectsReply, int messageType, int vendorId
           // network.sendIAMRouter(myNet);
            while(true){
                //Thread.sleep(1000);
             //   network.sendIAMRouter(myNet);
            }
        } catch (Exception e) {
            mstptransport.terminate();
            e.printStackTrace();
        }
    }
}
