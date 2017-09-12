package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 实现远程接口及远程方法（继承UnicastRemoteObject）
 */
public class Hello extends UnicastRemoteObject implements IApi {

    private String msg;
    protected Hello() throws RemoteException {}

    /**
     * 远程接口方法的实现
     * @return
     * @throws RemoteException
     */
    @Override
    public String say() throws RemoteException {
        msg="miqi";
        return msg;
    }
}
