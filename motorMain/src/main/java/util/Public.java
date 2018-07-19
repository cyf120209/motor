package util;

import com.pi4j.wiringpi.Gpio;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2017/9/14.
 */
public class Public {

    /**
     * 正则匹配
     * @param str
     * @param regx
     * @return true：匹配到，false：未匹配到
     */
    public static Boolean matchString(String str, String regx) {
        //1.将正在表达式封装成对象Patten 类来实现
        Pattern pattern = Pattern.compile(regx);
        //2.将字符串和正则表达式相关联
        Matcher matcher = pattern.matcher(str);
        //3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
//        System.out.println(matcher.matches());
//        String group = "";
        //查找符合规则的子串
        while (matcher.find()) {
            //获取 字符串
//            group = matcher.group();
            //获取的字符串的首位置和末位置
//            System.out.println(matcher.start() + "--" + matcher.end());
            return true;
        }
        return false;
    }

    /**
     * 则匹配
     * @param str
     * @param regx
     * @return 返回匹配到的第一个字符串
     */
    public static String matchStr(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
//        System.out.println(matcher.matches());
        String group = "";
        while (matcher.find()) {
            group = matcher.group();
//            System.out.println(matcher.start() + "--" + matcher.end());
            return group;
        }
        return "";
    }

    /**
     * 正则匹配
     * @param str
     * @param regx
     * @return 返回匹配到所有的字符串
     */
    public static String getAllString(String str, String regx) {
        //1.将正在表达式封装成对象Patten 类来实现
        Pattern pattern = Pattern.compile(regx);
        //2.将字符串和正则表达式相关联
        Matcher matcher = pattern.matcher(str);
        //3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
//        System.out.println(matcher.matches());
        String group = "";
        //查找符合规则的子串
        while (matcher.find()) {
            //获取 字符串
            group += matcher.group();
            //获取的字符串的首位置和末位置
//            System.out.println(matcher.start() + "--" + matcher.end());
        }
        return group;
    }

    /**
     * 读取modelName
     *
     * @param remoteDevice
     * @return
     */
    public static synchronized String readModelName(RemoteDevice remoteDevice) {
        try {
            LocalDevice localDevice = MyLocalDevice.getInstance();
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.modelName));
            return ack.getValue().toString();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 读取版本号
     *
     * @param remoteDevice
     * @return
     */
    public static synchronized String readVersion(RemoteDevice remoteDevice) {
        try {
            LocalDevice localDevice = MyLocalDevice.getInstance();
            ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(remoteDevice.getObjectIdentifier(), PropertyIdentifier.firmwareRevision));
            return ack.getValue().toString();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public synchronized void save(String str, String fileName) {
        File file = new File("log");
        String path = file.getAbsolutePath();
        if (!file.exists()) {
            file.mkdirs();
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
        String dateString = formatter.format(currentTime);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = fmt.format(currentTime);
        try {
            FileOutputStream fos = new FileOutputStream(new File(file, dateString + fileName), true);
            str = date + "    " + str;
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int crcCalcData(byte[] dataValues) {
        int crc=0;
        for (byte tmp : dataValues)
            crc = calcDataCRC(tmp & 0xff, crc);
        return crc;
    }

    private static int calcDataCRC(int dataValue, int crcValue) {
        int crcLow = (crcValue & 0xff) ^ dataValue; /* XOR C7..C0 with D7..D0 */
        /* Exclusive OR the terms in the table (top down) */
        int crc = (crcValue >> 8) ^ (crcLow << 8) ^ (crcLow << 3) ^ (crcLow << 12) ^ (crcLow >> 4) ^ (crcLow & 0x0f)
                ^ ((crcLow & 0x0f) << 7);
        return crc & 0xffff;
    }

    /**
     *
     * @param day 一年之中的第几天
     * @param lat 纬度 （度）
     * @param hour 时间（小时）
     * @return 太阳高度角 单位 弧度
     */
    public static double solarElevationAngle (int day, double lat, double hour) {
        if (hour >= 12){
            hour -= 12;
        }
        else{
            hour += 12;
        }
        double pi=3.1415926;
        double b=2*pi*(day-1)/365;
        double radLat=Math.toRadians(lat);   //观测点纬度（单位rad）
        double radTime=Math.toRadians(hour*15); //时角（单位rad）= 时间*15度
        double sDec=0.006918-0.399912*Math.cos(b)+0.070257*Math.sin(b)-0.006758*Math.cos(2*b)+
                0.000907* Math.sin(2*b)-0.002697*Math.cos(3*b)+0.00148*Math.sin(3*b);
        double sh=Math.sin(radLat)*Math.sin(sDec)+Math.cos(radLat)*Math.cos(sDec)*Math.cos(radTime);

//        System.out.println("radTime: "+radLat);
//        System.out.println("sDec: "+sDec);
//        System.out.println("hs: "+Math.toDegrees(Math.asin(sh)));
        return Math.asin(sh);
    }

    /**
     *
     * @param hour 当前时间
     * @param day 一年中的第几天
     * @param lat 当地纬度
     * @param sh 太阳高度角 （弧度）
     * @return 太阳方位角 （度）
     */
    public static double solarAzimuth(double hour, int day, double lat,double sh) {
        double pi=3.1415926;
        double b=2*pi*(day-1)/365;
        double radLat=Math.toRadians(lat);   //观测点纬度（单位rad）
        double sDec=0.006918-0.399912*Math.cos(b)+0.070257*Math.sin(b)-0.006758*Math.cos(2*b)+
                0.000907* Math.sin(2*b)-0.002697*Math.cos(3*b)+0.00148*Math.sin(3*b);
        double solarAzimuth = (Math.sin(sDec) - Math.sin(sh) * Math.sin(radLat)) / (Math.cos(sh) * Math.cos(radLat));
        if(hour>12){
            return 2*3.1415926-Math.acos(solarAzimuth);
        }else {
            return Math.acos(solarAzimuth);
        }
//        return (-Math.sin(0)*Math.cos(0.409))/(Math.cos(pi/2));
        //Math.sin(0)*Math.sin(0.409)+Math.sin(0)/(Math.cos(0)*Math.cos(0.409));
    }

    /**
     *
     * @param day
     * @param hour （小时）
     * @param sh 太阳高度角 （弧度）
     * @return 太阳方位角 （弧度）
     */
    public static double solarAzimuthAsin(int day,double hour,double sh) {
        double pi=3.1415926;
        double b=2*pi*(day-1)/365;
        if (hour >= 12){
            hour -= 12;
        }
        else{
            hour += 12;
        }
        double radTime=Math.toRadians(hour*15); //时角（单位rad）= 时间*15度
//        double radLat=Math.toRadians(lat);   //观测点纬度（单位rad）
        double sDec=0.006918-0.399912*Math.cos(b)+0.070257*Math.sin(b)-0.006758*Math.cos(2*b)+
                0.000907* Math.sin(2*b)-0.002697*Math.cos(3*b)+0.00148*Math.sin(3*b);

        return (-Math.sin(radTime)*Math.cos(sDec))/(Math.cos(sh));
    }


    /** Script file */
    private static final File TIME_SCRIPT_FILE = new File(
            System.getProperty("user.home"),"time-config-script");
    private static final File DATE_SCRIPT_FILE = new File(
            System.getProperty("user.home"),"date-config-script");
    private static final File SHUTDOWN_SCRIPT_FILE = new File(
            System.getProperty("user.home"),"shutdown");

    public static void createTimerScriptFile(String time) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(TIME_SCRIPT_FILE))) {
            bw.write("#!/bin/bash");
            bw.newLine();
            bw.write("sudo date -s "+time);
            bw.newLine();
            bw.write("echo time-script complete!");
            bw.newLine();
        }
        TIME_SCRIPT_FILE.setExecutable(true);
        TIME_SCRIPT_FILE.deleteOnExit();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.home")+"/time-config-script");
        pb.redirectErrorStream();
        Process p = pb.start();
        final InputStream is = p.getInputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int c;
                try {
                    while ((c = is.read()) != -1)
                        System.out.print((char)c);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }

    public static void createDateScriptFile(String date) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(DATE_SCRIPT_FILE))) {
            bw.write("#!/bin/bash");
            bw.newLine();
            bw.write("sudo date -s "+date);
            bw.newLine();
            bw.write("echo time-script complete!");
            bw.newLine();
        }
        DATE_SCRIPT_FILE.setExecutable(true);
        DATE_SCRIPT_FILE.deleteOnExit();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.home")+"/date-config-script");
        pb.redirectErrorStream();
        Process p = pb.start();
        final InputStream is = p.getInputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int c;
                try {
                    while ((c = is.read()) != -1)
                        System.out.print((char)c);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }

    public static void createShutdownScriptFile() throws IOException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Gpio.digitalWrite(1, Gpio.LOW);
            Thread.sleep(30);
            Gpio.digitalWrite(1, Gpio.HIGH);
            Thread.sleep(30);
        }
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(SHUTDOWN_SCRIPT_FILE))) {
            bw.write("#!/bin/bash");
            bw.newLine();
            bw.write("sudo shutdown -h now");
            bw.newLine();
            bw.write("echo time-script complete!");
            bw.newLine();
        }
        SHUTDOWN_SCRIPT_FILE.setExecutable(true);
        SHUTDOWN_SCRIPT_FILE.deleteOnExit();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.home")+"/shutdown");
        pb.redirectErrorStream();
        Process p = pb.start();
//        final InputStream is = p.getInputStream();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int c;
//                try {
//                    while ((c = is.read()) != -1)
//                        System.out.print((char)c);
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                }
//            }
//        }).start();
    }

    public static int week2Int(String week){
        if(week.equals("Sun")){
            return 1;
        }else if(week.equals("Mon")){
            return 2;
        }else if(week.equals("Tue")){
            return 3;
        }else if(week.equals("Wed")){
            return 4;
        }else if(week.equals("Thu")){
            return 5;
        }else if(week.equals("Fri")){
            return 6;
        }else if(week.equals("Sat")){
            return 7;
        }else {
            return -1;
        }
    }
}
