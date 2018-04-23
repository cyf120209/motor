package manager.impl;

import manager.rmi.IUpgrade;
import manager.rmi.IUpgradeCallback;
import model.FirmWareInformation;
import model.FirmWareResult;
import update.UpgradeImpl;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UpgradeManager extends UnicastRemoteObject implements IUpgrade{

    private final UpgradeImpl mUpgrade;

    public UpgradeManager() throws RemoteException {
        mUpgrade = new UpgradeImpl();
    }

    @Override
    public FirmWareResult chooseFirmware(String path) throws RemoteException {
        FirmWareResult firmWareResult = mUpgrade.chooseFirmware(path);
        return firmWareResult;
    }

    @Override
    public void startUpdate(IUpgradeCallback callback) throws RemoteException {
        mUpgrade.startUpdate(callback);
    }

    @Override
    public void startUpdate() throws RemoteException {

    }
}
