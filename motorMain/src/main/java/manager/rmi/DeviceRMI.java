package manager.rmi;

import dao.DeviceDao;
import entity.Device;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class DeviceRMI extends UnicastRemoteObject implements IDevice {

    public DeviceRMI() throws RemoteException {
    }

    @Override
    public List<Device> getDeviceList() throws RemoteException {
        DeviceDao deviceDao = new DeviceDao();
        return deviceDao.queryAll();
    }

    @Override
    public Device getByDeviceId(int deviceId) throws RemoteException {
        DeviceDao deviceDao = new DeviceDao();
        return deviceDao.selectByDeviceId(deviceId);
    }
}
