package limitsAndStops.presenter;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import limitsAndStops.view.LimitsAndStopsView;
import model.DraperInformation;
import rx.Observable;
import rx.functions.Func1;
import util.MyLocalDevice;
import util.Draper;
import util.Public;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class LimitsAndStopsPresenterImpl implements LimitsAndStopsPresenter{

    private final CfgResult cfgResult;
    private List<RemoteDevice> mRemoteDevices=new ArrayList<>();
    private LocalDevice localDevice;
    public LimitsAndStopsView mLimitsAndStopsView;

    public LimitsAndStopsPresenterImpl(LimitsAndStopsView mLimitsAndStopsView) {
        this.mLimitsAndStopsView = mLimitsAndStopsView;
        localDevice = MyLocalDevice.getInstance();
        List<RemoteDevice> remoteDeviceList = MyLocalDevice.getRemoteDeviceList();
        for (RemoteDevice remoteDevice:remoteDeviceList){
            if(Public.matchString(remoteDevice.getModelName(),"MC-AC")){
                mRemoteDevices.add(remoteDevice);
            }
        }
        mLimitsAndStopsView.updateDevBox(mRemoteDevices);
        cfgResult = new CfgResult();
//        save("asdf");
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

    @Override
    public void configuration(int id, int distance, int step) {
        try {
            save("configuration: id="+id+" distance="+distance+"\r\n");
            Draper.sendConfigure(id,distance,step);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void  save(int cmd){
        if(!mLimitsAndStopsView.getRunningState()){
            return;
        }
        save("command: "+cmd+"\r\n");
    }

    @Override
    public void saveRemoteDeviceInfomation() {
        try {
            for(RemoteDevice remoteDevice:mRemoteDevices){
                DraperInformation draperInformation = Draper.sendAnnounceDraperInformation(remoteDevice, true);
                save(draperInformation.toString());
            }
        }catch (Exception e){
        }
    }

    @Override
    public void saveRemoteDeviceCfg() {
        int success=0;
        int fail=0;
        try {
            for(RemoteDevice remoteDevice:mRemoteDevices){

                DraperInformation draperInformation = Draper.sendAnnounceDraperInformation(remoteDevice, true);
                int lowerLimit = draperInformation.getLowerLimit().intValue();
                int upperLimit = draperInformation.getUpperLimit().intValue();
                if(lowerLimit-upperLimit==mLimitsAndStopsView.getDistance()){
                    success++;
                }else {
                    fail++;
                }
                save(draperInformation.toString());
                save(draperInformation.getId()+"    "+(lowerLimit - upperLimit)+"\r\n");
            }
            if(fail==0){
                cfgResult.success();
            }
            save("成功"+success+"台，失败"+fail+"台   成功"+cfgResult.getSuccess()+"次，失败"+cfgResult.getFail()+"次\r\n\r\n");
        }catch (Exception e){

        }
    }

    @Override
    public void clearCfgCount() {
        cfgResult.clear();
    }

    public synchronized void save(String str){
        File file = new File("log");
        String path = file.getAbsolutePath();
        if(!file.exists()){
            file.mkdirs();
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
        String dateString = formatter.format(currentTime);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = fmt.format(currentTime);
        try {
            FileOutputStream fos = new FileOutputStream(new File(file, dateString+"-log.txt"),true);
            str=date+"    "+str;
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
