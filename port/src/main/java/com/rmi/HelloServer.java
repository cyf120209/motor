package com.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by lenovo on 2017/4/17.
 */
public class HelloServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            HelloInterface hello = new Hello("hello world");
            Naming.rebind("Hello",hello);
            System.out.println("Hello Server is ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
