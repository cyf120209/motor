package com.rmi;

import java.io.NotSerializableException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lenovo on 2017/4/17.
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
            HelloInterface hello = (HelloInterface)Naming.lookup("hello");
            User user = hello.getUser();
            System.out.println(user.getName());
            User user1=hello.setUser("cyf");
            System.out.println(user1.getName());
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotSerializableException e) {
            e.printStackTrace();
        }
    }
}
