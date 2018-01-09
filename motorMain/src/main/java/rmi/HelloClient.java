package rmi;

import java.io.NotSerializableException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by lenovo on 2017/4/17.
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
            IApi hello = (IApi)Naming.lookup("hello");
            String say = hello.say();
            System.out.println(say);
//            User user1=hello.setUser("cyf");
//            System.out.println(user1.getName());
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
