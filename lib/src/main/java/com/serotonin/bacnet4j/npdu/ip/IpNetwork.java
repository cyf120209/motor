/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.npdu.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.event.ExceptionDispatch;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.*;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.primitive.OctetString;

import com.serotonin.bacnet4j.util.Byte2IntUtils;
import org.apache.log4j.Logger;
import org.free.bacnet4j.util.ByteQueue;

public class IpNetwork extends Network{
	private static final Logger LOG = Logger.getLogger(IpNetwork.class);
    public static final String DEFAULT_BROADCAST_IP = "192.168.20.255";
    public static final int DEFAULT_PORT = 0xBAC0; // == 47808
    public static final int OTHER_PORT = 0xBABF; // == 47807
    public static final String DEFAULT_BIND_IP = "0.0.0.0";

    private static final int MESSAGE_LENGTH = 2048;
    private final int port;
    private final String localBindAddress;
    private final String broadcastIp;

    // Runtime
    final private Thread incomingMessageProcessor;
    private DatagramSocket socket;
    private Address broadcastAddress;

    public IpNetwork() {
        this(DEFAULT_BROADCAST_IP);
    }

    public IpNetwork(String broadcastIp) {
        this(broadcastIp, DEFAULT_PORT);
    }

    public IpNetwork(String broadcastIp, int port) {
        this(broadcastIp, port, DEFAULT_BIND_IP);
    }

    public IpNetwork(String broadcastIp, int port, String localBindAddress) {
        this(broadcastIp, port, localBindAddress, 0);
    }

    public IpNetwork(final String broadcastIp, 
    			     final int port, 
    			     final String localBindAddress, 
    			     final int localNetworkNumber) {
        super(localNetworkNumber);
        this.broadcastIp = broadcastIp;
        this.port = port;
        this.localBindAddress = localBindAddress;
        this.incomingMessageProcessor = new IncomingMessageProcessor();
    }

    @Override
    public NetworkIdentifier getNetworkIdentifier() {
        return new IpNetworkIdentifier(port, localBindAddress);
    }

    @Override
    public MaxApduLength getMaxApduLength() {
        return MaxApduLength.UP_TO_1476;
    }

    public int getPort() {
        return port;
    }

    public String getLocalBindAddress() {
        return localBindAddress;
    }

    public String getBroadcastIp() {
        return broadcastIp;
    }
    
    @Override
    public void initialize(Transport transport) throws Exception {
        super.initialize(transport);
        if (localBindAddress.equals("0.0.0.0"))
            socket = new DatagramSocket(port);
        else
            socket = new DatagramSocket(port, InetAddress.getByName(localBindAddress));
        socket.setBroadcast(true);

        //broadcastAddress = new Address(broadcastIp, port, new Network(0xffff, new byte[0]));
        broadcastAddress = new Address(BACnetUtils.dottedStringToBytes(broadcastIp), port);
        incomingMessageProcessor.start();
    }

    @Override
    public void terminate() {
        if (socket != null)
            socket.close();
    }

    @Override
    public Address getLocalBroadcastAddress() {
        return broadcastAddress;
    }

    public Address getBroadcastAddress(int port) {
        return new Address(BACnetUtils.dottedStringToBytes(broadcastIp), port);
    }

    @Override
    public void checkSendThread() {
        if (Thread.currentThread() == incomingMessageProcessor)
            throw new IllegalStateException("Cannot send a request in the socket listener thread.");
    }

    @Override
    public void SendIamRouter(List<Integer> list) {
        ByteQueue queue = new ByteQueue();

        // BACnet virtual link layer detail

        // BACnet/IP
        queue.push(0x81);

        // Original-Unicast-NPDU, or Original-Broadcast-NPDU
        queue.push( 0xb );

        NPCI npci = new NPCI(Address.GLOBAL, null,false,1,900);
        ByteQueue postLength = new ByteQueue();
        npci.write(postLength);
        for (int i:list){
            postLength.pushU2B(i);
        }
        BACnetUtils.pushShort(queue, queue.size() + postLength.size() + 2);

        queue.push(postLength);
        // Combine the queues

        InetSocketAddress isa;
        isa = getLocalBroadcastAddress().getMacAddress().getInetSocketAddress();
        try {
            sendPacket(isa, queue.popAll());
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    /** The total length of the foreign device registration package. */
    private static final int REGISTER_FOREIGN_DEVICE_LENGTH = 6;

    /**
     * Sends a foreign device registration request to addr. On successful registration (AKN), we are added
     * in the foreign device table FDT.
     * 
     * @param addr
     *            The address of the device where our device wants to be registered as foreign device
     * @param timeToLive
     *            The time until we are automatically removed out of the FDT.
     * @throws BACnetException
     */
    public void sendRegisterForeignDeviceMessage(InetSocketAddress addr, int timeToLive) throws BACnetException {
        ByteQueue queue = new ByteQueue();

        // BACnet/IP
        queue.push(0x81);

        // Register foreign device
        queue.push(0x05);

        BACnetUtils.pushShort(queue, REGISTER_FOREIGN_DEVICE_LENGTH);
        BACnetUtils.pushShort(queue, timeToLive);

        sendPacket(addr, queue.popAll());
    }

    @Override
    public void sendAPDU(Address recipient, OctetString link, APDU apdu, boolean broadcast) throws BACnetException {
        ByteQueue queue = new ByteQueue();

        // BACnet virtual link layer detail

        // BACnet/IP
        queue.push(0x81);

        // Original-Unicast-NPDU, or Original-Broadcast-NPDU
        queue.push(broadcast ? 0xb : 0xa);

        // NPCI
        ByteQueue postLength = new ByteQueue();
        writeNpci(postLength, recipient, link, apdu);

        // APDU
        apdu.write(postLength);

        // Length
        BACnetUtils.pushShort(queue, queue.size() + postLength.size() + 2);

        // Combine the queues
        queue.push(postLength);

        InetSocketAddress isa;
        if (recipient.isGlobal())
            isa = getLocalBroadcastAddress().getMacAddress().getInetSocketAddress();
        else if (link != null)
            isa = link.getInetSocketAddress();
        else
            isa = recipient.getMacAddress().getInetSocketAddress();
        sendPacket(isa, queue.popAll());
    }

    @Override
    public void sendNPDU(Address recipient, NPDU npdu) {
        ByteQueue queue = new ByteQueue();

        // BACnet virtual link layer detail

        // BACnet/IP
        queue.push(0x81);

        // Original-Unicast-NPDU, or Original-Broadcast-NPDU
        queue.push( 0xb);

        // NPCI
        ByteQueue postLength = new ByteQueue();
        npdu.write(postLength);

        // Length
        BACnetUtils.pushShort(queue, queue.size() + postLength.size() + 2);

        // Combine the queues
        queue.push(postLength);

        InetSocketAddress isa;
        if (recipient.isGlobal())
            isa = getLocalBroadcastAddress().getMacAddress().getInetSocketAddress();
        else if (npdu.npci.hasDestinationInfo())
            isa = new Address(npdu.npci.getDestinationNetwork(),npdu.npci.getDestinationAddress()).getMacAddress().getInetSocketAddress();
        else
            isa = recipient.getMacAddress().getInetSocketAddress();
        try {
            sendPacket(isa, queue.popAll());
        } catch (BACnetException e) {
            e.printStackTrace();
        }
    }

    public void rcvFrame(ByteQueue frame,OctetString linkService){
        Address from, des;
        if (frame.pop() != (byte) 0x81)
            return;

        final byte function = frame.pop();
        if (function != 0xa && function != 0xb &&
                function != 0x4 && function != 0x0)
            return;
        final int length = BACnetUtils.popShort(frame);
        if (length != frame.size() + 4)
            return;
        // Answer to foreign device registration
        if (function == 0x0) {
            final int result = BACnetUtils.popShort(frame);
            if (result != 0)
                LOG.info("Foreign device registration not successful! result: " + result);
            // not APDU received, bail
            return;
        }
        if (function == 0x4) {
            // A forward. Use the address/port as the link service address.
            byte[] addr = new byte[6];
            frame.pop(addr);
            linkService = new OctetString(addr);
        }
        NPCI npci = new NPCI(frame);
        if (npci.getVersion() != 1)
            try {
                throw new MessageValidationAssertionException("Invalid protocol version: " + npci.getVersion());
            } catch (MessageValidationAssertionException e) {
                e.printStackTrace();
            }
        if (npci.isNetworkMessage())
            return ; // throw new MessageValidationAssertionException("Network messages are not supported");

        // Check the destination network number work and do not respond to foreign networks requests
        if (npci.hasDestinationInfo()) {
            des = new Address(npci.getDestinationNetwork(), npci.getDestinationAddress());
        } else {
            des = Address.GLOBAL;
        }

        if (npci.hasSourceInfo())
            from = new Address(npci.getSourceNetwork(), npci.getSourceAddress());
        else
            from = new Address(linkService);
        if(npci.hasDestinationInfo()){
            System.out.println("ip--DestinationInfo"+npci.getDestinationNetwork());
            if(npci.getDestinationNetwork()==2001||npci.getDestinationNetwork()== 65535){
                MstpNetwork.routeCount++;
            }
        }else{
            return;
        }
        npci.setSourceAddress(new Address(1,linkService));
        npci.setDestinationAddress(null);
        NPDU npdu=new NPDU(npci,frame);
        this.peerNet.sendNPDU(des,npdu);
        return;
    }

    private void sendPacket(InetSocketAddress addr, byte[] data) throws BACnetException {
        try {
            String s = Byte2IntUtils.bytesToHexString(data);
            System.out.println("sendPacket---"+s);
            DatagramPacket packet = new DatagramPacket(data, data.length, addr);
            socket.send(packet);
        }
        catch (Exception e) {
            throw new BACnetException(e);
        }
    }

    private class IncomingMessageProcessor extends Thread{
    	/**
    	 * Runnable that handles incoming messages
    	 */
    	@Override
    	public void run() {
    		byte[] buffer = new byte[MESSAGE_LENGTH];
    		final DatagramPacket p = new DatagramPacket(buffer, buffer.length);
    		while (!socket.isClosed()) {
    			try {
    				socket.receive(p);
    				final ByteQueue queue = new ByteQueue(p.getData(), 0, p.getLength());
    				final OctetString link = new OctetString(p.getAddress().getAddress(), 
    						p.getPort());
                    if(InetAddress.getLocalHost().equals(p.getAddress()) && p.getPort()==OTHER_PORT){
                        continue;
                    }
                    IpNetwork.this.rcvFrame((ByteQueue)queue.clone(),link);

//                    IncomingRequestProcessor.getIncomingRequestProcessor().submit(
//    				new IncomingMessageExecutor(IpNetwork.this, queue, link));
//    				p.setData(buffer);
    			}
    			catch (IOException e) {
    				// no op. This happens if the socket gets closed by the destroy method.
    			}
    		}
    	}
    }

    public APDU createApdu(byte[] message) throws Exception {
        IncomingMessageExecutor ime = new IncomingMessageExecutor(null, new ByteQueue(message), null);
        return ime.parseApdu();
    }

//  public void testDecoding(byte[] message) {
//  IncomingMessageExecutor ime = new IncomingMessageExecutor(null, new ByteQueue(message), null);
//  ime.run();
//}


    static class IncomingMessageExecutor extends IncomingRequestParser {
    	
        public IncomingMessageExecutor(final Network network, final ByteQueue queue,
        							   final OctetString localFrom) {
            super(network, queue, localFrom);
        }

        @Override
        protected void parseFrame() throws MessageValidationAssertionException {
            // Initial parsing of IP message.
            // BACnet/IP
            if (queue.pop() != (byte) 0x81)
                throw new MessageValidationAssertionException("Protocol id is not BACnet/IP (0x81)");

            final byte function = queue.pop();
            if (function != 0xa && function != 0xb && 
            	function != 0x4 && function != 0x0)
                throw new MessageValidationAssertionException("Function is not "
                		+ "unicast, broadcast, forward or foreign device reg "
                		+ "anwser (0xa, 0xb, 0x4 or 0x0)");
            final int length = BACnetUtils.popShort(queue);
            if (length != queue.size() + 4)
                throw new MessageValidationAssertionException("Length field does"
                		+ " not match data: given=" + length + ", expected=" 
                		+ (queue.size() + 4));
            // Answer to foreign device registration
            if (function == 0x0) {
                final int result = BACnetUtils.popShort(queue);
                if (result != 0)
                    LOG.info("Foreign device registration not successful! result: " + result);
                // not APDU received, bail
                return;
            }
            if (function == 0x4) {
                // A forward. Use the address/port as the link service address.
                byte[] addr = new byte[6];
                queue.pop(addr);
                linkService = new OctetString(addr);
            }
        }
    }

    //
    //
    // Convenience methods
    //
    public Address getAddress(InetAddress inetAddress) {
        try {
            return new Address(getLocalNetworkNumber(), inetAddress.getAddress(), port);
        }
        catch (Exception e) {
            // Should never happen, so just wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }

    public static InetAddress getDefaultLocalInetAddress() throws UnknownHostException, SocketException {
        for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress addr : Collections.list(iface.getInetAddresses())) {
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress())
                    return addr;
            }
        }
        return InetAddress.getLocalHost();
    }

    @Override
    public Address[] getAllLocalAddresses() {
        try {
            ArrayList<Address> result = new ArrayList<Address>();
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(iface.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress())
                        result.add(getAddress(addr));
                }
            }

            return result.toArray(new Address[result.size()]);
        }
        catch (Exception e) {
            // Should never happen, so just wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((broadcastIp == null) ? 0 : broadcastIp.hashCode());
        result = prime * result + ((localBindAddress == null) ? 0 : localBindAddress.hashCode());
        result = prime * result + port;
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
        final IpNetwork other = (IpNetwork) obj;
        if (broadcastIp == null) {
            if (other.broadcastIp != null)
                return false;
        }
        else if (!broadcastIp.equals(other.broadcastIp))
            return false;
        if (localBindAddress == null) {
            if (other.localBindAddress != null)
                return false;
        }
        else if (!localBindAddress.equals(other.localBindAddress))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
}
