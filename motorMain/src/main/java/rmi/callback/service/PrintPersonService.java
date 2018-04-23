package rmi.callback.service;

import rmi.callback.entity.PersonEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface PrintPersonService extends Remote{
    public void printPerson(List<PersonEntity> persons) throws RemoteException;
    public void printPerson(Integer i) throws RemoteException;
}