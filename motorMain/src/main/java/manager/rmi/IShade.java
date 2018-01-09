package manager.rmi;

import entity.Shade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *  创建远程接口及声明远程方法
 *  远程接口必须扩展java.rmi.remote
 */
public interface IShade extends Remote {

    /**
     * 远程接口方法必须抛出* java.rmi.RemoteException
     * @return
     * @throws RemoteException
     */
    public List<Shade> getShadeList() throws RemoteException;

    public Shade getByShadeId(int shadeId) throws RemoteException;
}
