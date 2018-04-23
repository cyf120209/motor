package rmi.callback.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PersonService extends Remote{
    public void getPersons(PrintPersonService printService) throws RemoteException;
}
