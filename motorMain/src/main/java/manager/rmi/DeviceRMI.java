package manager.rmi;

import com.serotonin.bacnet4j.RemoteDevice;
import dao.DeviceDao;
import entity.Device;
import util.Draper;
import util.MyLocalDevice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

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

    @Override
    public Boolean updateDevice(Device device) throws RemoteException {
        DeviceDao deviceDao = new DeviceDao();
        int update = deviceDao.update(device);
        if(update==1){
            return true;
        }else {
            return false;
        }
    }
}
