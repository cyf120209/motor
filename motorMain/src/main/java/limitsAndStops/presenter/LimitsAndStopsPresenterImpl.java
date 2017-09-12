package limitsAndStops.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import limitsAndStops.view.LimitsAndStopsView;
import util.MyLocalDevice;
import util.Draper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class LimitsAndStopsPresenterImpl implements LimitsAndStopsPresenter{

    private List<RemoteDevice> mRemoteDevices=new ArrayList<>();
    private LocalDevice localDevice;
    public LimitsAndStopsView mLimitsAndStopsView;

    public LimitsAndStopsPresenterImpl(LimitsAndStopsView mLimitsAndStopsView) {
        this.mLimitsAndStopsView = mLimitsAndStopsView;
        localDevice = MyLocalDevice.getInstance();
        mLimitsAndStopsView.updateDevBox(MyLocalDevice.getRemoteDeviceList());
//        getDraperInformation();
    }

    @Override
    public void extended() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void retracted() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void JOpen() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void JClose() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void upperLimit() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lowerLimit() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preStop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextStop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2004);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remoteStop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2005);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remoteAllStop() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2006);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reverse() {
        try {
            Draper.sendCmd(mLimitsAndStopsView.getSelectedItem(),2007);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDraperInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Draper.sendAnnounceDraperInformation(mLimitsAndStopsView.getSelectedItem());
                } catch (BACnetException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();

    }
}
