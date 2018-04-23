package rmi.callback.registry;

import manager.rmi.IUpgrade;
import rmi.callback.service.PersonService;
import rmi.callback.service.PersonServiceImpl;
import manager.impl.UpgradeManager;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class ServiceRegistry {
    public static void main(String[] args) {//服务端程序入口
        try {
            PersonService personService = new PersonServiceImpl();
            IUpgrade upgradeManager = new UpgradeManager();
            LocateRegistry.createRegistry(12312);
            Naming.bind("rmi://127.0.0.1:12312/PersonService", personService);
            Naming.bind("rmi://127.0.0.1:12312/upgrade", upgradeManager);
            System.out.println("Service started...");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
