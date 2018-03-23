package manager.impl;

import manager.rmi.IUpgrade;
import model.FirmWareInformation;
import update.UpgradeImpl;
import util.Public;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UpgradeManager extends UnicastRemoteObject implements IUpgrade{

    private final UpgradeImpl mUpgrade;

    protected UpgradeManager() throws RemoteException {
        mUpgrade = new UpgradeImpl();
    }

    @Override
    public FirmWareInformation chooseFirmware(String path) throws RemoteException {
        return mUpgrade.chooseFirmware(path);
    }

    @Override
    public void startUpdate() throws RemoteException {
        mUpgrade.startUpdate();
    }

    @Override
    public int getUpgradeProgress() throws RemoteException {
        return mUpgrade.getUpgradeProgress();
    }
}
