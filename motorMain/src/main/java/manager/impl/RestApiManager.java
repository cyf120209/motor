package manager.impl;

import common.Common;
import manager.rmi.*;
import manager.rmi.IUpgrade;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RestApiManager {

    public static void init(){
        try {
            LocateRegistry.createRegistry(12312);
            IDevice device = new DeviceRMI();
            IGroup group = new GroupRMI();
            IShade shade = new ShadeRMI();
            ILog log = new LogRMI();
            IUpgrade upgradeManager = new UpgradeManager();
            /**
             * 如果要把hello实例注册到另一台启动了RMI注册服务的机器上
             * Naming.rebind("//192.168.1.105:1099/device", device)
             */
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/device",device);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/group",group);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/shade",shade);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/log",log);
            Naming.rebind("//"+ Common.HOST_IP+":"+Common.RMI_PORT+"/upgrade",upgradeManager);
            System.out.println("RMI register succeed");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
