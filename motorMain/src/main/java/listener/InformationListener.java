package listener;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import main.presenter.BoxLayoutCasePresenter;
import main.view.BoxLayoutView;
import show.ShowAllDevice;
import util.MyLocalDevice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/19.
 */
public class InformationListener extends DeviceEventAdapter {

    public LocalDevice localDevice;
    ShowAllDevice mShowAllDevice;

    /**
     * 去重处理
     */
    private List<Integer> remoteDeviceIDList=new ArrayList<>();

    public InformationListener(ShowAllDevice boxLayoutView) {
        this.localDevice = MyLocalDevice.getInstance();
        this.mShowAllDevice=boxLayoutView;
    }

    @Override
    public void iAmReceived(final RemoteDevice d) {
        Integer id = Integer.valueOf(d.getInstanceNumber());
        boolean exist = remoteDeviceIDList.contains(id);
        if (exist) {
            return;
        }
        remoteDeviceIDList.add(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                    mShowAllDevice.showDevice(d);
            }
        }).start();
    }

    public void clearRemoteDeviceList() {
        this.remoteDeviceIDList.clear();
    }

}