package com;

import gnu.io.CommPortIdentifier;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by lenovo on 2017/4/6.
 */
public class MainClass {

    public static void main(String[] args) {
//        List<String> listName=new ArrayList<String>();
//        System.out.println("COM1");
//        Enumeration enumeration= CommPortIdentifier.getPortIdentifiers();
//        System.out.println("COM2");
//        CommPortIdentifier portId;
//        System.out.println("COM3");
//        while(enumeration.hasMoreElements()){
//            System.out.println("COM4");
//            portId=(CommPortIdentifier)enumeration.nextElement();
//            System.out.println("COM5");
//            if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {
//                System.out.println(portId.getName());
////                listName.add( portId.getName());
//            }
//        }
        try {
            String localIP = WebToolUtils.getLocalIP();
            System.out.println(localIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
