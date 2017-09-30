package show;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import listener.InformationListener;
import util.MyLocalDevice;
import util.Public;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/9/30.
 */
public class ShowAllDevice extends JFrame implements ActionListener{

    public JButton refresh=new JButton("refresh");
    public JList showInformation = new JList();
    JScrollPane showInformationJSP = new JScrollPane(showInformation);

    LocalDevice localDevice;
    InformationListener informationListener;

    public ShowAllDevice() throws HeadlessException {
        setTitle("update");
        setLayout(null);
        setSize(350, 500);

        refresh.setBounds(10,10,100,20);
        showInformationJSP.setBounds(10, 40, 320, 420);

        add(refresh);
        add(showInformationJSP);

        refresh.addActionListener(this);

        informationListener = new InformationListener(this);
        localDevice = MyLocalDevice.getInstance();
        localDevice.getEventHandler().addListener(informationListener);

        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                localDevice.getEventHandler().removeListener(informationListener);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(refresh.equals(e.getSource())){
            try {
                informationListener.clearRemoteDeviceList();
                odv.clear();
                showInformation.setModel(odv);
                localDevice.sendGlobalBroadcast(new WhoIsRequest());
            } catch (BACnetException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void getInformations() {
        List<RemoteDevice> remoteDeviceList = MyLocalDevice.getRemoteDeviceList();
        for(RemoteDevice remoteDevice:remoteDeviceList){
            String modelName = Public.readModelName(remoteDevice);
            String version = Public.readVersion(remoteDevice);
            show(remoteDevice.getInstanceNumber()+"----"+modelName+"----"+version);
        }
    }

    public void showDevice(RemoteDevice remoteDevice){
        String modelName = Public.readModelName(remoteDevice);
        String version = Public.readVersion(remoteDevice);
        show(remoteDevice.getInstanceNumber()+"----"+modelName+"----"+version);
    }

    DefaultListModel<String> odv = new DefaultListModel<>();

    private void show(final String info) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    odv.addElement(info);
                    showInformation.setModel(odv);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
