package manager.rmi;

import entity.Log;
import entity.ShadeGroup;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ILog extends Remote{

    List<Log> getLogList() throws RemoteException;

    Log getByLogId(int id) throws RemoteException;

}
