package manager.rmi;

import dao.DeviceDao;
import dao.ShadeDao;
import entity.Device;
import entity.Shade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
}
