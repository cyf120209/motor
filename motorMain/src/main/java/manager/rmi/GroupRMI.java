package manager.rmi;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import dao.GroupDao;
import entity.ShadeGroup;
import groupOperation.presenter.GroupOperationPresenterImpl;
import manager.impl.DatabaseManager;
import model.DeviceGroup;
import util.Draper;
import util.MyLocalDevice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class GroupRMI extends UnicastRemoteObject implements IGroup{

    public GroupRMI() throws RemoteException {
    }

    @Override
    public List<ShadeGroup> getGroupList() throws RemoteException {
        GroupDao groupDao = new GroupDao();
        List<ShadeGroup> shadeGroupList = groupDao.queryAll();
        return shadeGroupList;
    }

    @Override
    public ShadeGroup getByGroupId(int id) throws RemoteException {
        GroupDao groupDao = new GroupDao();
        return groupDao.selectByGroupId(id);
    }

    @Override
    public Boolean groupSubscriptionToSelect(Integer id, Boolean remove, Integer deviceId, Integer groupId) throws RemoteException {
        return addGroup(id, remove, deviceId, groupId);
    }

    private synchronized Boolean addGroup(Integer id, Boolean remove, Integer deviceId, Integer groupId) {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.mRemoteUtils.getRemoteDeviceMap();
        try {
            Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(id),remove,deviceId,groupId);
            Thread.sleep(50);
            GroupOperationPresenterImpl.announce();
            Thread.sleep(500);
            DatabaseManager.updateGroupThread();
            return true;
        } catch (BACnetException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
