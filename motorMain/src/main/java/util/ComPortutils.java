package util;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class ComPortutils {
    public static  LinkedList<String> listPort() {
        LinkedList<String> listName=new LinkedList<String>();
        Enumeration enumeration= CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier portId;
        while(enumeration.hasMoreElements()){
            portId=(CommPortIdentifier)enumeration.nextElement();
            if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL) {
                listName.add( portId.getName());
            }
        }
        return listName;
    }
}
