package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * 启动RMI注册服务，并注册远程对象
 */
public class HelloServer {
    /**
     * 启动RMI注册服务并进行对象注册
     * @param args
     */
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            IApi hello = new Hello();
            /**
             * 如果要把hello实例注册到另一台启动了RMI注册服务的机器上
             * Naming.rebind("//192.168.1.105:1099/Hello", hello)
             */
            Naming.rebind("hello",hello);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
