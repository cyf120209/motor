package manager.rmi;


import model.FirmWareInformation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IUpgrade extends Remote {

    /**
     * 远程接口方法必须抛出* java.rmi.RemoteException
     * @return
     * @throws RemoteException
     */
    public FirmWareInformation chooseFirmware(String path) throws RemoteException;

    public void startUpdate() throws RemoteException;

    public int getUpgradeProgress() throws RemoteException;

}
