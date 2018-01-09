package manager.rmi;

import dao.GroupDao;
import entity.ShadeGroup;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
}
