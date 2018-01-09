package manager.impl;

import common.Common;
import dao.DeviceDao;
import entity.Device;
import manager.rmi.DeviceRMI;
import manager.rmi.GroupRMI;
import manager.rmi.ShadeRMI;
import rmi.Hello;
import rmi.IApi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RestApiManager {

    public static void init(){
        try {
            LocateRegistry.createRegistry(12312);
            DeviceRMI device = new DeviceRMI();
            GroupRMI group = new GroupRMI();
            ShadeRMI shade = new ShadeRMI();
            /**
             * 如果要把hello实例注册到另一台启动了RMI注册服务的机器上
             * Naming.rebind("//192.168.1.105:1099/device", device)
             */
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/shade",shade);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/device",device);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/group",group);
            System.out.println("RMI register succeed");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
