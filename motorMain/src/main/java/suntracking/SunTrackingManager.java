package suntracking;

import model.DeviceGroup;
import model.ShadeParameter;
import model.ShutterParameter;
import pojo.LngLat;
import suntracking.presenter.SuntrackingPresenter;
import util.Draper;
import util.FileUtils;
import util.GsonUtils;
import util.Public;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;

public class SunTrackingManager {

    double pi=3.1415926;
    private static SuntrackingPresenter mSuntrackingPresenter;
    private int lastShutterCmd=0;
    private int lastShadeCmd=0;

    public static void init(SuntrackingPresenter suntrackingPresenter){
        mSuntrackingPresenter=suntrackingPresenter;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SunTrackingManager().start();
            }
        }).run();
    }


//    double[] hours=new double[]{
//            1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24
//    };

//    double[] hours=new double[]{
//            5,6,7,8,9,10,11,12,13,14,15,16,17,18,19
//    };

    private void start(){
//        caculate();
        new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto();
            }
        }).start();

    }

    private void auto() {
        LngLat lngLat = mSuntrackingPresenter.getLngLat();
        List<DeviceGroup> deviceGroupList = mSuntrackingPresenter.getDeviceGroup();
        if(lngLat==null || deviceGroupList==null || deviceGroupList.size()==0){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_YEAR);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m=calendar.get(Calendar.MINUTE);
        double hour=h+m/60.0;
        double lat = lngLat.getLat();
        System.out.println("auto: "+lat);
//        double lat=23.43;

        double hs = Public.solarElevationAngle(day, lat, hour);
        double solarAzimuth = Public.solarAzimuth(hour, day, lat, hs);

        ShadeParameter shadeParameter = mSuntrackingPresenter.getShadeParameter();
        if(shadeParameter!=null) {
//            double percent = calClothPercent(0.6, 1.8, 1.2, hs);
            int percent1 = calClothPercent(shadeParameter.getShadeShade(), shadeParameter.getShadeHeight(), shadeParameter.getShadeLength(), shadeParameter.getShadeInclination(), hs, solarAzimuth, shadeParameter.getShadeAzimuth());
            System.out.println("shadeCmd"+percent1);
            if(percent1!=lastShadeCmd) {
                lastShadeCmd=percent1;
                sendCmd(deviceGroupList, 100 + percent1, 7);
            }
        }
        ShutterParameter shutterParameter = mSuntrackingPresenter.getShutterParameter();
        if(shutterParameter!=null) {
//            double rad = calShutter(8, 7, 0, hs, solarAzimuth, pi / 2);
            double rad = calShutter(shutterParameter.getShutterWidth(), shutterParameter.getShutterClearance(), shutterParameter.getShutterInclination(), hs, solarAzimuth, shutterParameter.getShutterAzimuth());
            int shutterCmd= (int)Math.ceil(rad * 100 * 2 / pi);
            System.out.println("shutterCmd"+shutterCmd);
            if(shutterCmd!=lastShutterCmd){
                lastShutterCmd=shutterCmd;
                try {
                    sendCmd(deviceGroupList,shutterCmd,10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("");
    }

    private void sendCmd(List<DeviceGroup> deviceGroupList,int cmd,int priority){
        try {
            for (DeviceGroup deviceGroup:deviceGroupList) {
                Draper.sendCmd(deviceGroup.getDeviceId(), deviceGroup.getGroupId(), cmd, priority);
//                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void caculate() {
        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_YEAR);
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);

        day=357;
//        double lat=0;
        double lat=23.43;
        double[] hours=new double[12];
        for(int i=0;i<12;i++){
            hours[i]=5+i*10.0/60;
        }
        for (double hour : hours) {
            hour=6+43/60.0;
            double hs = Public.solarElevationAngle(day, lat, hour);
            System.out.println("hour: "+hour+" hs: "+Math.toDegrees(hs));
            double solarAzimuth = Public.solarAzimuth(hour, day, lat, hs);
            System.out.println("solarAzimuth: " + Math.toDegrees(solarAzimuth));

//            double percent = calClothPercent(0.6, 1.8, 1.2, hs);
//            System.out.println("percent: " + percent);
//            int percent1 = calClothPercent(0.6, 1.8, 1.2, 0 ,hs, solarAzimuth,pi/2);
//            System.out.println("cloth percent1: " + percent1);

//            Draper.sendCmd(100+percent1);

//            double atan = Math.atan(Math.tan(hs) / Math.cos(solarAzimuth));
//            System.out.println("hs1: " + Math.toDegrees(atan));



//            double rad1 = calShutter(8, 7, hs, solarAzimuth, pi / 2);
//            System.out.println("rad1: "+Math.toDegrees(rad1));
            double rad = calShutter(8, 7, 0, hs, solarAzimuth, pi / 2);
            System.out.println("deg: "+Math.toDegrees(rad));

            System.out.println("");

        }
    }

    private double calClothPercent(double shade,double clothHeight,double clothLength,double hs){
        double percent = (clothHeight - shade * Math.tan(hs)) / clothLength;
        return percent;
    }

    /**
     *      电机
     * @param shade 进入的光线长度
     * @param clothHeight 窗帘距离地面的高度
     * @param clothLength 窗帘的长度
     * @param inclination 窗帘的倾斜角度 垂直于地面为0度
     * @param hs 太阳高度角
     * @param hsa 太阳方位角
     * @param sa 窗帘的方位角
     * @return 窗帘应下降的百分比
     */
    private int calClothPercent(double shade,double clothHeight,double clothLength,double inclination, double hs,double hsa,double sa){
        double pi=3.1415926;
        double rad=0;
        if(Math.abs(hsa-sa)>90 && Math.abs(hsa-sa)<270){
            return 0;
        }
        if(Math.abs(hsa-sa)<pi/2){
            rad=Math.abs(hsa-sa);
        }else if(hsa-sa>3*pi/2){
            rad=2*pi-hsa+sa;
        }else if(hsa-sa<-3*pi/2){
            rad=2*pi+hsa-sa;
        }
        double percent = 100 * (clothHeight - shade * Math.tan(hs) / Math.cos(rad)) / (Math.sin( pi/2 -inclination) * clothLength);
//        System.out.println("hs: "+Math.toDegrees(hs) +" a: "+Math.toDegrees(Math.atan(Math.tan(hs)/Math.cos(hsa))));

        if(percent<0){
            return 0;
        }else if (percent>100){
            return 100;
        }else {
            return (int)percent;
        }
    }

    private double calShutter(double width,double height,double hs,double hsa, double sa){
        double pi=3.1415926;
        double azimuth=0;
        if(Math.abs(hsa-sa)>90 && Math.abs(hsa-sa)<270){
            return 0;
        }
        if(Math.abs(hsa-sa)<pi/2){
            azimuth=Math.abs(hsa-sa);
        }else if(hsa-sa>3*pi/2){
            azimuth=2*pi-hsa+sa;
        }else if(hsa-sa<-3*pi/2){
            azimuth=2*pi+hsa-sa;
        }
        double rad = Math.asin(height * Math.sin(pi / 2 - hs) / width) - hs;
        return (rad<0)?0:rad;
    }

    /**
     *  百叶窗
     * @param width 百叶窗宽度
     * @param height 百叶窗长度
     * @param inclination 百叶窗倾斜角度
     * @param hs 太阳高度角
     * @param hsa 太阳方位角
     * @param sa 百叶窗方位角
     * @return
     */
    private double calShutter(double width,double height,double inclination,double hs,double hsa, double sa){
        double pi=3.1415926;
        double azimuth=0;
        if(Math.abs(hsa-sa)>90 && Math.abs(hsa-sa)<270){
            return 0;
        }
        if(Math.abs(hsa-sa)<pi/2){
            azimuth=Math.abs(hsa-sa);
        }else if(hsa-sa>3*pi/2){
            azimuth=2*pi-hsa+sa;
        }else if(hsa-sa<-3*pi/2){
            azimuth=2*pi+hsa-sa;
        }
        double rad = Math.asin(height * Math.sin(pi / 2 + inclination - Math.atan(Math.tan(hs) / Math.cos(azimuth))) / width) +
                inclination - Math.atan(Math.tan(hs) / Math.cos(azimuth));
        return (rad<0)?0:rad;
    }

    private void test(){

    }

}
