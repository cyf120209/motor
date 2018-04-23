package manager.rmi;


import model.FirmWareInformation;
import model.FirmWareResult;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IUpgrade extends Remote {

    /**
     * 远程接口方法必须抛出* java.rmi.RemoteException
     * @return
     * @throws RemoteException
     */
    public FirmWareResult chooseFirmware(String path) throws RemoteException;

    /**
     *
     * @param callback 升级信息回调
     * @throws RemoteException
     */
    public void startUpdate(IUpgradeCallback callback) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    public void startUpdate() throws RemoteException;

}
