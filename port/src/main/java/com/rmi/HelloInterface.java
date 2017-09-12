package com.rmi;

import java.io.NotSerializableException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by lenovo on 2017/4/17.
 */
public interface HelloInterface extends Remote{

    public String say() throws RemoteException;
    public User getUser() throws RemoteException,NotSerializableException;
    public User setUser(String username) throws RemoteException,NotSerializableException;
}
