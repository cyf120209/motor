package manager.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUpgradeCallback extends Remote{

    void onStart() throws RemoteException;

    void onCancel() throws RemoteException;

    void onFinish() throws RemoteException;

    void onProgressChanged(Integer masterProgress,Integer slaveProgress) throws RemoteException;

    void showLog(String masterInfo,String slaveInfo) throws RemoteException;

}
