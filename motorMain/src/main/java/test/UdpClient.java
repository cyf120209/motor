package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient {

    private static DatagramSocket datagramSocket;

    public static void main(String[] args) throws IOException {
        /*** 发送数据***/
        // 初始化datagramSocket,注意与前面Server端实现的差别
        datagramSocket = new DatagramSocket();
        // 使用DatagramPacket(byte buf[], int length, InetAddress address, int port)函数组装发送UDP数据报
        String sendStr="asdfasdfasdfasdfasdf";
        byte[] buf = sendStr.getBytes();
        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length,address,6001);
        // 发送数据
        datagramSocket.send(datagramPacket);
    }
}
