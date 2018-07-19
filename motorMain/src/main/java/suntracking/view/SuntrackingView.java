package suntracking.view;

import com.qt.datapicker.DatePicker;
import com.serotonin.bacnet4j.RemoteDevice;
import common.Common;
import groupOperation.IGroupCallback;
import groupOperation.view.GroupOperation;
import model.DeviceGroup;
import model.ShadeParameter;
import model.ShutterParameter;
import pojo.City;
import pojo.LngLat;
import pojo.States;
import suntracking.presenter.SuntrackingPresenter;
import suntracking.presenter.SuntrackingPresenterImpl;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SuntrackingView extends JFrame implements ActionListener, ISuntrackingView {

    private JLabel jlDate = new JLabel("y-M-d");
    private JTextField jtDate = new JTextField();
    private JButton jbChooseDate = new JButton("c");
    private JButton jbSetDate = new JButton("set date");
    private JLabel jlTime = new JLabel("H:m");
    private JTextField jtTime = new JTextField();
    private JButton jbSetTime = new JButton("set time");
    private JButton jbAnnounce = new JButton("Announce");

    private JComboBox hour;
    private JComboBox min;

    public JComboBox jbState = new JComboBox();
    public JComboBox jbCity = new JComboBox();
    public JTextField jfLng = new JTextField();
    public JTextField jfLat = new JTextField();
    public JLabel jlAddLng = new JLabel("lng");
    public JLabel jlAddLat = new JLabel("lat");
    public JTextField jfAddLng = new JTextField("118.169875");
    public JTextField jfAddLat = new JTextField("24.531222");
    public JButton jbSetLngLat = new JButton("setLngLat");

    private JLabel jlShadeHeight=new JLabel("窗帘的离地高度");
    private JLabel jlShadeLength=new JLabel("窗帘的长度");
    private JLabel jlShadeInclination=new JLabel("Inclination");
    private JLabel jlShadeAzimuth=new JLabel("Azimuth");
    private JLabel jlShadeShade=new JLabel("阴影");
    public JTextField jfShadeHeight = new JTextField();
    public JTextField jfShadeLength = new JTextField();
    public JTextField jfShadeInclination = new JTextField();
    public JTextField jfShadeAzimuth = new JTextField();
    public JTextField jfShadeShade = new JTextField();
    public JButton jbSetShade=new JButton("set");

    private JLabel jlShutterWidth=new JLabel("百叶窗扇叶的宽度");
    private JLabel jlShutterClearance=new JLabel("百叶窗扇叶的间距");
    private JLabel jlShutterInclination=new JLabel("Inclination");
    private JLabel jlShutterAzimuth=new JLabel("Azimuth");
    public JTextField jfShutterWidth = new JTextField();
    public JTextField jfShutterClearance = new JTextField();
    public JTextField jfShutterInclination = new JTextField();
    public JTextField jfShutterAzimuth = new JTextField();
    public JButton jbSetShutter=new JButton("set");


    public JTextField jfCmd = new JTextField();
    public JButton jbCmd=new JButton("sent");


    private List<States> usa;
    private List<City> cityList;
    private LngLat lngLat;
    private SuntrackingPresenter mSuntrackingPresenter;

    public SuntrackingView() throws HeadlessException {
        setTitle("Suntracking");
        setSize(Common.SCREEN_WEIGHT, Common.SCREEN_HEIGHT);

        setLocationRelativeTo(this);

        setLayout(null);
        //总在最前面
//        setAlwaysOnTop(true);
        //不能改变大小
        setResizable(false);
        //最大化
//        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //不要边框 需放置在组件添加之前，否则不生效
//        setUndecorated(true);

        // 把背景图片显示在一个标签里面
        JLabel label = new JLabel(StyleUtils.getFormBg());
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        jbState.setBounds(5, 5, 100, 20);
        jbCity.setBounds(110, 5, 100, 20);
        jfLng.setBounds(230, 5, 100, 20);
        jfLat.setBounds(330, 5, 100, 20);
        jlAddLng.setBounds(5,35,40,20);
        jlAddLat.setBounds(5,55,40,20);
        jfAddLng.setBounds(45, 35, 100, 20);
        jfAddLat.setBounds(45, 55, 100, 20);
        jbSetLngLat.setBounds(170, 55, 100, 20);

        jbAnnounce.setBounds(450, 5, 100, 20);

        jfCmd.setBounds(5, 5, 100, 20);
        jbCmd.setBounds(110, 5, 100, 20);

        add(jbState);
        add(jbCity);
        add(jfLng);
        add(jfLat);
        add(jlAddLng);
        add(jlAddLat);
        add(jfAddLng);
        add(jfAddLat);
        add(jbSetLngLat);
        add(jbAnnounce);

        StyleUtils.setBtnBg(jbSetLngLat);
        StyleUtils.setBtnBg(jbAnnounce);

        Integer[] h = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        Integer[] m = new Integer[60];
        for (int i = 0; i < 60; i++) {
            m[i] = i + 1;
        }
        Integer[] p = new Integer[100];
        for (int i = 0; i < 100; i++) {
            p[i] = i + 1;
        }

        Panel pDate = new Panel();
        pDate.setLayout(null);
        pDate.setBounds(5, 80, 250, 50);
        jlDate.setBounds(5, 5, 40, 20);
        jtDate.setBounds(50, 5, 80, 20);
        jbChooseDate.setBounds(130, 5, 20, 20);
        jbSetDate.setBounds(150, 5, 100, 20);
        jlTime.setBounds(5, 30, 40, 20);
        jtTime.setBounds(50, 30, 80, 20);
        jbSetTime.setBounds(150, 30, 100, 20);
        pDate.add(jlDate);
        pDate.add(jtDate);
        pDate.add(jbChooseDate);
        pDate.add(jbSetDate);
        pDate.add(jlTime);
        pDate.add(jtTime);
        pDate.add(jbSetTime);
        jbChooseDate.addActionListener(this);
        jbSetDate.addActionListener(this);
        jbSetTime.addActionListener(this);
        add(pDate);

        hour = new JComboBox(h);
        min = new JComboBox(m);

        hour.setBounds(5, 60, 100, 20);
        min.setBounds(110, 60, 100, 20);
//        add(hour);
//        add(min);
//        add(jbSetTime);


        Panel pShade = new Panel();
        pShade.setLayout(null);
        pShade.setBounds(5, 150, 250, 125);
        jlShadeHeight.setBounds(5,5,120,20);
        jlShadeLength.setBounds(5,25,120,20);
        jlShadeInclination.setBounds(5,45,120,20);
        jlShadeAzimuth.setBounds(5,65,120,20);
        jlShadeShade.setBounds(5,85,120,20);

        jfShadeHeight.setBounds(120,5,100,20);
        jfShadeLength.setBounds(120,25,100,20);
        jfShadeInclination.setBounds(120,45,100,20);
        jfShadeAzimuth.setBounds(120,65,100,20);
        jfShadeShade.setBounds(120,85,100,20);

        jbSetShade.setBounds(5,105,100,20);

        pShade.add(jlShadeHeight);
        pShade.add(jlShadeLength);
        pShade.add(jlShadeInclination);
        pShade.add(jlShadeAzimuth);
        pShade.add(jlShadeShade);
        pShade.add(jfShadeHeight);
        pShade.add(jfShadeLength);
        pShade.add(jfShadeInclination);
        pShade.add(jfShadeAzimuth);
        pShade.add(jfShadeShade);
        pShade.add(jbSetShade);

        jbSetShade.addActionListener(this);
        add(pShade);


        Panel pShutter = new Panel();
        pShutter.setLayout(null);
        pShutter.setBounds(5, 280, 250, 120);

        jlShutterWidth.setBounds(5,5,120,20);
        jlShutterClearance.setBounds(5,25,120,20);
        jlShutterInclination.setBounds(5,45,120,20);
        jlShutterAzimuth.setBounds(5,70,65,20);

        jfShutterWidth.setBounds(120,5,100,20);
        jfShutterClearance.setBounds(120,25,100,20);
        jfShutterInclination.setBounds(120,45,100,20);
        jfShutterAzimuth.setBounds(120,65,100,20);

        jbSetShutter.setBounds(5,85,100,20);


        pShutter.add(jlShutterWidth);
        pShutter.add(jlShutterClearance);
        pShutter.add(jlShutterInclination);
        pShutter.add(jlShutterAzimuth);
        pShutter.add(jfShutterWidth);
        pShutter.add(jfShutterClearance);
        pShutter.add(jfShutterInclination);
        pShutter.add(jfShutterAzimuth);
        pShutter.add(jbSetShutter);

        jbSetShutter.addActionListener(this);
        add(pShutter);

        showExistedGroup();

        initData();

        jbState.addActionListener(this);
        jbCity.addActionListener(this);
        jbSetLngLat.addActionListener(this);
        jbSetTime.addActionListener(this);
        jbAnnounce.addActionListener(this);

        mSuntrackingPresenter = new SuntrackingPresenterImpl(this);
    }

    private void initData() {
        ClassLoader classLoader = SuntrackingView.class.getClassLoader();
        URL resource = classLoader.getResource("");
//        String path = resource.getPath();
        URL resource1 = SuntrackingView.class.getResource("/");
        System.out.println("resource1: " + resource1);
//        System.out.println("getResource(\"/\"): "+path);
        URL url = SuntrackingView.class.getResource("/usa_state_city.txt");
        System.out.println("url: " + url);
//        String filePath = path + "usa_state_city.txt";
        /*String s = FileUtils.readFile(url.getPath());
        Country country = GsonUtils.parseJSON(s, Country.class);
        usa = country.getUsa();
        jbState.addItem("choose state");
        jbCity.addItem("choose city");
        for (States states:usa){
            jbState.addItem(states.getStateName());
        }*/
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(calendar.getTime());
        jtDate.setText(date);
        jtTime.setText(time);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jbState)) {
            int stateSelectedIndex = jbState.getSelectedIndex();
            if (stateSelectedIndex <= 0) return;
            States states = usa.get(stateSelectedIndex - 1);
            cityList = states.getCities();
            jbCity.removeAllItems();
            jbCity.addItem("choose city");
            for (City city : cityList) {
                String cityName = city.getCityName();
                if (!cityName.equals("null")) {
                    jbCity.addItem(cityName);
                }
            }
        } else if (e.getSource().equals(jbCity)) {
            int citySelectedIndex = jbCity.getSelectedIndex();
            if (citySelectedIndex <= 0) return;
            City city = cityList.get(citySelectedIndex - 1);
            lngLat = new LngLat(city.getLng(), city.getLat());
            jfLng.setText("" + lngLat.getLng());
            jfLat.setText("" + lngLat.getLat());
            mSuntrackingPresenter.setLngLat(lngLat);
        } else if (e.getSource().equals(jbSetLngLat)) {
            String n = jfAddLng.getText().toString().trim();
            String a = jfAddLat.getText().toString().trim();
            LngLat lngLat = new LngLat(Double.parseDouble(n), Double.parseDouble(a));
            mSuntrackingPresenter.setLngLat(lngLat);
        } else if (e.getSource().equals(jbChooseDate)) {
            chooseDate();
        } else if (e.getSource().equals(jbSetDate)) {
            try {
                Public.createDateScriptFile(jtDate.getText().toString().trim());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource().equals(jbSetTime)) {
            try {
                Public.createTimerScriptFile(jtTime.getText().toString().trim());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource().equals(jbIdentify)) {
            identify();
        } else if (e.getSource().equals(jbAnnounce)) {
            mSuntrackingPresenter.sendAnnounce();
//            Object[] arr=new Object[]{1,2,3};
//            updateExistedGroup(arr);
        } else if(e.getSource().equals(jbSetShade)){
            setShadeParameter();
        } else if(e.getSource().equals(jbSetShutter)){
            setShutterParameter();
        }else if (e.getSource().equals(btnEditGroup)) {
            editGroup();
        }
    }

    private void editGroup() {
        GroupOperation groupOperation = new GroupOperation(new IGroupCallback() {
            @Override
            public void updateExistedGroup(Object[] arr) {
                SuntrackingView.this.updateExistedGroup(arr);
            }
        });
        groupOperation.setLocationRelativeTo(null);
        groupOperation.setVisible(true);
    }

    private void setShutterParameter() {
        String width = jfShutterWidth.getText().toString().trim();
        String clearance = jfShutterClearance.getText().toString().trim();
        String inclination = jfShutterInclination.getText().toString().trim();
        String azimuth = jfShutterAzimuth.getText().toString().trim();
        ShutterParameter shutterParameter = new ShutterParameter(Double.valueOf(width),
                Double.valueOf(clearance),
                Math.toRadians(Double.valueOf(inclination)),
                Math.toRadians(Double.valueOf(azimuth)));
        mSuntrackingPresenter.setShutterParameter(shutterParameter);
    }

    private void setShadeParameter() {
        String height = jfShadeHeight.getText().toString().trim();
        String length = jfShadeLength.getText().toString().trim();
        String shade = jfShadeShade.getText().toString().trim();
        String inclination = jfShadeInclination.getText().toString().trim();
        String azimuth = jfShadeAzimuth.getText().toString().trim();
        ShadeParameter shadeParameter = new ShadeParameter(Double.valueOf(height),
                Double.valueOf(length),
                Double.valueOf(shade),
                Math.toRadians(Double.valueOf(inclination)),
                Math.toRadians(Double.valueOf(azimuth)));
        mSuntrackingPresenter.setShadeParameter(shadeParameter);
    }

    private void chooseDate() {
        new ObservingTextField().getTimer();
    }

    private void identify() {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        for (int i = 0; i < jcbGroup.length; i++) {
            if (jcbGroup[i].isSelected()) {
                DeviceGroup deviceGroup = deviceGroupList.get(i);
                try {
                    Draper.sendCmd(deviceGroup.getDeviceId(),deviceGroup.getGroupId(), 1602);
                    Thread.sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Panel existedGroup = new Panel();
    JButton jbIdentify=new JButton("identify select");
    JButton btnEditGroup =new JButton("edit group");

    private void showExistedGroup() {
        existedGroup.setBounds(270, 80, 300, 300);
        jbIdentify.setBounds(270, 380, 150, 20);
        btnEditGroup.setBounds(430,380,100,20);
        add(existedGroup);
        add(jbIdentify);
        add(btnEditGroup);
        jbIdentify.addActionListener(this);
        btnEditGroup.addActionListener(this);
    }

    JCheckBox[] jcbGroup;
    List<DeviceGroup> deviceGroupList=new ArrayList<>();

    @Override
    public void updateExistedGroup(Object[] deviceList) {
        deviceGroupList.clear();
        existedGroup.removeAll();
        Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.mRemoteUtils.getRelationMap();
        for(Object o:deviceList){
            Integer deviceId = Integer.valueOf(String.valueOf(o));
            Map<Integer, List<Integer>> groupListMap = relationMap.get(deviceId);
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = groupListMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, List<Integer>> group = iterator.next();
                Integer groupId = group.getKey();
                List<Integer> shadeList = group.getValue();
                deviceGroupList.add(new DeviceGroup(deviceId,groupId,shadeList));
            }
        }
        jcbGroup = new JCheckBox[deviceGroupList.size()];
        for (int i = 0; i < deviceGroupList.size(); i++) {
            jcbGroup[i] = new JCheckBox(deviceGroupList.get(i).toString());
//            idGroupList.add(Integer.valueOf(String.valueOf(arr[i])));
        }
        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));
        for (int i = 0; i < deviceGroupList.size(); i++) {
            existedGroup.add(jcbGroup[i]);
        }
//        existedGroup.invalidate();
        existedGroup.validate();
        existedGroup.repaint();
    }

    List<DeviceGroup> deviceGroupSelectList=new ArrayList<>();

    @Override
    public List<DeviceGroup> getDeviceGroup() {
        deviceGroupSelectList.clear();
        for(int i=0;i<jcbGroup.length;i++){
            if(jcbGroup[i].isSelected()){
                deviceGroupSelectList.add(deviceGroupList.get(i));
            }
        }
        return deviceGroupSelectList;
    }

    class ObservingTextField extends JTextField implements Observer {

        private ObservingTextField jtf7;
        private JButton btn;

        public void update(Observable o, Object arg) {
            Calendar calendar = (Calendar) arg;
            DatePicker dp = (DatePicker) o;
            String date = dp.formatDate(calendar);
            jtDate.setText("20"+date);
        }

        private void getTimer() {
            jtf7 = new ObservingTextField();
            jtf7.setLocation(300, 200);
            jtf7.setEditable(false);
            btn = new JButton("日历");
            DatePicker dp = new DatePicker(jtf7, Locale.CHINA);
            // previously selected date
            Date selectedDate = dp.parseDate(jtf7.getText());
            dp.setSelectedDate(selectedDate);
            dp.start(jtf7);
            jtf7.setBounds(140, 230, 110, 23);
            btn.setBounds(260, 230, 60, 23);
            this.add(jtf7);
            this.add(btn);
        }
    }

    public void showError(String str) {
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSuccess(String str) {
        JOptionPane.showMessageDialog(null, str, "success", JOptionPane.PLAIN_MESSAGE);
    }
}
