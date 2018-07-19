package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import entity.Device;
import main.presenter.BoxLayoutCasePresenter;
import main.view.BoxLayoutView;
import util.MyLocalDevice;
import util.Public;
import util.RemoteUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2017/1/19.
 */
public class BaseListener extends DeviceEventAdapter {

    public LocalDevice localDevice;

    /**
     * 去重处理
     */
    private Set<Device> remoteDeviceSet =null;

    public BaseListener() {
        this.localDevice = MyLocalDevice.getInstance();

    }

    @Override
    public void iAmReceived(final RemoteDevice remoteDevice) {
        RemoteUtils remoteUtils = MyLocalDevice.mRemoteUtils;
        remoteDeviceSet=MyLocalDevice.mRemoteUtils.getDeviceSet();
        Device device = new Device(remoteDevice.getInstanceNumber(), remoteDevice.getAddress().getMacAddress().toString());
        if (remoteDeviceSet.contains(device)) {
            return;
        }
//        byte mstpAddress = remoteDevice.getAddress().getMacAddress().getMstpAddress();
//        System.out.println(""+mstpAddress);
        System.out.println("++++++++++++++"+remoteDevice.getInstanceNumber());
        remoteUtils.addRemoteDevice(device,remoteDevice);
    }
}