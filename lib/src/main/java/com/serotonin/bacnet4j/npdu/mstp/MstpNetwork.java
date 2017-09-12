package com.serotonin.bacnet4j.npdu.mstp;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.event.ExceptionDispatch;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.*;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

import java.util.List;

public class MstpNetwork extends Network {
    private final MstpNode node;

    public static int routeCount=0;

    public MstpNetwork(MstpNode node) {
        this(node, 0);
    }

    public MstpNetwork(MstpNode node, int localNetworkNumber) {
        super(localNetworkNumber);
        this.node = node;
        node.setNetwork(this);
    }

    @Override
    public MaxApduLength getMaxApduLength() {
        return MaxApduLength.UP_TO_480;
    }

    @Override
    public void initialize(Transport transport) throws Exception {
        super.initialize(transport);
        node.initialize();
    }

    @Override
    public void terminate() {
        node.terminate();
    }

    @Override
    public NetworkIdentifier getNetworkIdentifier() {
        return new MstpNetworkIdentifier(node.getCommPortId());
    }

    @Override
    public Address getLocalBroadcastAddress() {
        return new Address(getLocalNetworkNumber(), (byte) 0xFF);
    }

    @Override
    public Address[] getAllLocalAddresses() {
        return new Address[]{new Address(getLocalNetworkNumber(), node.getThisStation())};
    }

    @Override
    public void checkSendThread() {
        if (Thread.currentThread() == node.thread)
            throw new IllegalStateException("Cannot send a request in the socket listener thread.");
    }

    @Override
    public void SendIamRouter(List<Integer> list) {
        //Address source, boolean expectsReply, int messageType, int vendorId
        ByteQueue queue = new ByteQueue();
        NPCI npci = new NPCI(Address.GLOBAL, null,false,1,900);
        npci.write(queue);
        for (int i:list){
            queue.pushU2B(i);
        }
        byte[] data = queue.popAll();
        node.setReplyFrame(FrameType.bacnetDataNotExpectingReply,(byte) 0xff, data);
    }

    @Override
    public void sendAPDU(Address recipient, OctetString link, APDU apdu, boolean broadcast) throws BACnetException {
        ByteQueue queue = new ByteQueue();

        // NPCI
        writeNpci(queue, recipient, link, apdu);

        // APDU
        apdu.write(queue);

        byte[] data = queue.popAll();

        byte mstpAddress;
        if (recipient.isGlobal())
            mstpAddress = getLocalBroadcastAddress().getMacAddress().getMstpAddress();
        else if (link != null)
            mstpAddress = link.getMstpAddress();
        else
            mstpAddress = recipient.getMacAddress().getMstpAddress();

        if (apdu.expectsReply()) {
            if (node instanceof SlaveNode)
                throw new RuntimeException("Cannot originate a request from a slave node");

            ((MasterNode) node).queueFrame(FrameType.bacnetDataExpectingReply, mstpAddress, data);
        } else
            node.setReplyFrame(FrameType.bacnetDataNotExpectingReply, mstpAddress, data);
    }

    @Override
    public void sendNPDU(Address recipient, NPDU npdu) {
        ByteQueue queue = new ByteQueue();

        // NPCI
        //npdu.npci.write(queue);
        npdu.write(queue);
        // APDU
        //npdu.apdu.write(queue);

        byte[] data = queue.popAll();

        byte mstpAddress;
        if (recipient.isGlobal())
            mstpAddress = getLocalBroadcastAddress().getMacAddress().getMstpAddress();
        else if (npdu.npci.getDestinationAddress() != null)
            mstpAddress =new OctetString(npdu.npci.getDestinationAddress()).getMstpAddress();
        else
            mstpAddress = recipient.getMacAddress().getMstpAddress();


        node.setReplyFrame(FrameType.bacnetDataNotExpectingReply, mstpAddress, data);
    }

    public void sendData(byte[] data){
        node.setReplyFrame(FrameType.bacnetDataNotExpectingReply, (byte)-1, data);
    }

    public void sendTestRequest(byte destination) {
        if (!(node instanceof MasterNode))
            throw new RuntimeException("Only master nodes can send test requests");
        ((MasterNode) node).queueFrame(FrameType.testRequest, destination, null);
    }

    //
    //
    //
    // Incoming frames
    //
    void receivedFrame(Frame frame) {
        if(enableRouter && routeCount>0) {
            Frame routerFrame = frame.copy();
            ByteQueue queue = new ByteQueue(routerFrame.getData());
            NPCI npci = new NPCI(queue);
            if(npci.hasDestinationInfo()){
                System.out.println("mstp--DestinationInfo"+npci.getDestinationNetwork());
                if(npci.getDestinationNetwork()==1 || npci.getDestinationNetwork()== 65535){
//                    return;
                }else {

                }
            }else{
//                return;
            }

            npci.setSourceAddress(new Address(2001,frame.getSourceAddress()));
            npci.setDestinationAddress(null);
            //Address destination, Address source, boolean expectsReply, int messageType, int vendorId
            if (npci.getVersion() != 1)
                try {
                    throw new MessageValidationAssertionException("Invalid protocol version: " + npci.getVersion());
                } catch (MessageValidationAssertionException e) {
                    e.printStackTrace();
                }
            if (npci.isNetworkMessage())
                return; // throw new MessageValidationAssertionException("Network messages are not supported");

            // Check the destination network number work and do not respond to foreign networks requests
            OctetString linkService = new OctetString(frame.getSourceAddress());
            Address from, des;
            if (npci.hasSourceInfo())
                from = new Address(npci.getSourceNetwork(), npci.getSourceAddress());
            else
                from = new Address(linkService);
            if (npci.hasDestinationInfo()) {
                des = new Address(npci.getDestinationNetwork(), npci.getDestinationAddress());
            } else {
                des = Address.GLOBAL;
            }
            routeCount--;
            System.out.println("routeCount"+routeCount);
            peerNet.sendNPDU(des, new NPDU(npci, queue));
        }
        new IncomingFrameHandler(this, frame).run();
    }

    class IncomingFrameHandler extends IncomingRequestParser {
        public IncomingFrameHandler(Network network, Frame frame) {
            super(network, new ByteQueue(frame.getData()), new OctetString(frame.getSourceAddress()));
        }

        @Override
        protected void parseFrame() throws MessageValidationAssertionException {
            // no op. The frame has already been parsed.
        }
    }
    //
    //
    // Convenience methods
    //
    public Address getAddress(byte station) {
        return new Address(getLocalNetworkNumber(), station);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MstpNetwork other = (MstpNetwork) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        return true;
    }
}
