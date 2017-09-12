package com;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by lenovo on 2017/5/5.
 */
public class UdpServer {

    public static void main(String[] args) {
        //监听9876端口
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(47808);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
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
            System.out.println("sendPacket---"+s);

//            String sentence = new String( receivePacket.getData());
//            System.out.println("RECEIVED: " + sentence);
        }
    }
}
