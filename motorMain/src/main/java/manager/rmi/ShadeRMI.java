package manager.rmi;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import dao.ShadeDao;
import entity.Shade;
import entity.DraperInformation;
import util.Draper;
import util.MyLocalDevice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ShadeRMI extends UnicastRemoteObject implements IShade {
    public ShadeRMI() throws RemoteException {
    }

    @Override
    public List<Shade> getShadeList() throws RemoteException {
        ShadeDao shadeDao = new ShadeDao();
        List<Shade> shadeList = shadeDao.queryAll();
        return shadeList;
    }

    @Override
    public Shade getByShadeId(int shadeId) throws RemoteException {
        ShadeDao shadeDao = new ShadeDao();
        Shade shade = shadeDao.selectByShadeId(shadeId);
        return shade;
    }

    @Override
    public void identify(Integer id) throws RemoteException {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.mRemoteUtils.getRemoteDeviceMap();
        try {
            Draper.sendCmd(remoteDeviceMap.get(id), 1602);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move(Integer id, Integer cmd) throws RemoteException {
        move(id, cmd,1);
    }

    @Override
    public void move(Integer id,Integer cmd,Integer cmdService) throws RemoteException{
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.mRemoteUtils.getRemoteDeviceMap();
        try {
            Draper.sendCmd(cmd,cmdService,remoteDeviceMap.get(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DraperInformation limitAndStopOperation(Integer id, Integer cmd) throws RemoteException {
        return limitAndStopOperation(id, cmd,1);
    }

    @Override
    public DraperInformation limitAndStopOperation(Integer id, Integer cmd, Integer cmdService) throws RemoteException {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.mRemoteUtils.getRemoteDeviceMap();
        try {
            Draper.sendCmd(cmd,cmdService,remoteDeviceMap.get(id));
            return getDraperInformation(id);
        } catch (BACnetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DraperInformation getDraperInformation(Integer id) throws RemoteException {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.mRemoteUtils.getRemoteDeviceMap();
        try {
            return Draper.sendAnnounceDraperInformation(remoteDeviceMap.get(id), true);
        } catch (BACnetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
