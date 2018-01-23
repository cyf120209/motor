package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * Created by lenovo on 2017/4/17.
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
//            System.setSecurityManager(new java.rmi.RMISecurityManager());
//            if (System.getSecurityManager() == null) {
//                System.setSecurityManager(new SecurityManager());
                System.setSecurityManager(new RMISecurityManager(){
                    public void checkConnect (String host, int port) {}
                    public void checkConnect (String host, int port, Object context) {}
                });
//            }
//            System.setProperty("java.security.policy", "AllPermission.policy");
            IApi hello = (IApi) Naming.lookup("hello");
            String say = hello.say();
            System.out.println(say);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
