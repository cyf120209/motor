package manager.rmi;

import entity.ShadeGroup;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGroup extends Remote{

    List<ShadeGroup> getGroupList() throws RemoteException;

    ShadeGroup getByGroupId(int id) throws RemoteException;

}
