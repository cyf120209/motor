package com.rmi;

import java.io.NotSerializableException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/17.
 */
public class Hello extends UnicastRemoteObject implements HelloInterface {

    private String message;

    private User user;
    protected Hello(String msg) throws RemoteException {
        message=msg;
        user=new User(msg);
        List<String> list=new ArrayList<String>();
        list.add("2");
        list.add("2");
        list.add("2");
        list.add("2");
        user.setList(list);
    }

    public String say() throws RemoteException {
        System.out.println("CalledbyHelloClient");
        return message;
    }

    public User getUser() throws RemoteException,NotSerializableException {
        return user;
    }

    public User setUser(String username) throws RemoteException, NotSerializableException {
        user.setName(username);
        return user;
    }
}
