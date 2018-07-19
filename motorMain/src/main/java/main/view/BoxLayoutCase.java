package main.view;

import groupOperation.view.GroupOperation;
import com.serotonin.bacnet4j.RemoteDevice;
import common.Common;
import limitsAndStops.view.LimitsAndStops;
import main.presenter.BoxLayoutCasePresenter;
import main.presenter.BoxLayoutCasePresenterImpl;
import schedule.view.ScheduleOnly;
import setting.SettingView;
import show.ShowAllDevice;
import suntracking.view.SuntrackingView;
import update.view.Update;
import util.ComPortutils;
import util.Public;
import util.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by lenovo on 2017/1/18.
 */
public class BoxLayoutCase extends JFrame implements ActionListener,BoxLayoutView{

    private BoxLayoutCasePresenter mBoxLayoutCasePresenter;

    public JButton startstopbt=new JButton("start");
    public JButton groupOperation=new JButton("groupOperation");
    public JButton update=new JButton("update");
    public JButton sunTracking=new JButton("sunTracking");
    public JButton setting=new JButton("setting");
//    public JButton database=new JButton("database");
    public JButton schedule=new JButton("schedule");
    public JButton showAllDevice=new JButton("showAllDevice");

    public JComboBox comBox=new JComboBox();
    public JButton upComBox=new JButton("update port");

    public JButton upBt=new JButton("up");
    public JButton downbt=new JButton("down");
    public JButton stopButton=new JButton("stop");

    public JComboBox devBox=new JComboBox();
    public JLabel versionLabel= new JLabel("Versin: No Version");
    public JLabel draperID= new JLabel("draperID: ");
    public JLabel address= new JLabel("address: ");

    public JTextField cmdTextEdit=new JTextField(10);
    public JButton cmdTestbt=new JButton("SendCmdBorcast");
    public JButton cmdTestOneBT=new JButton("SendCmd");

    public JButton limitsSetting=new JButton("LimitsSetting");

    private JButton exit=new JButton("关机");

    public JComboBox commType=new JComboBox();

    public BoxLayoutCase() throws HeadlessException {
        mBoxLayoutCasePresenter = new BoxLayoutCasePresenterImpl(this);
        setTitle("Draper");
        setSize(Common.SCREEN_WEIGHT,Common.SCREEN_HEIGHT);

        setLocationRelativeTo(this);

        setLayout(null);
        //总在最前面
//        setAlwaysOnTop(true);
        //不能改变大小
        setResizable(false);
        //最大化
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //不要边框 需放置在组件添加之前，否则不生效
        setUndecorated(true);

        // 把背景图片显示在一个标签里面
        JLabel label = new JLabel(StyleUtils.getFormBg());
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        startstopbt.setBounds(new Rectangle(10, 10, 100, Common.HEIGHT));
        groupOperation.setBounds(new Rectangle(110,10,150,Common.HEIGHT));
        update.setBounds(new Rectangle(260,10,100,Common.HEIGHT));
        showAllDevice.setBounds(new Rectangle(360,10,120,Common.HEIGHT));
        sunTracking.setBounds(new Rectangle(480,10,100,Common.HEIGHT));
        setting.setBounds(new Rectangle(480,30,100,Common.HEIGHT));
        schedule.setBounds(new Rectangle(580,10,100,Common.HEIGHT));
        comBox.setBounds(10,35,150, Common.HEIGHT);
        upComBox.setBounds(170,35,100,Common.HEIGHT);

        upBt.setBounds(new Rectangle(210, 130, 100, Common.HEIGHT));
        downbt.setBounds(new Rectangle(210, 170, 100, Common.HEIGHT));
        stopButton.setBounds(new Rectangle(210, 150, 100, Common.HEIGHT));

        devBox.setBounds(10,100,300,Common.HEIGHT);

        versionLabel.setBounds(10,130,200,Common.HEIGHT);
        draperID.setBounds(10,150,200,Common.HEIGHT);
        address.setBounds(10,170,200,Common.HEIGHT);

        cmdTextEdit.setBounds(10,200,100,Common.HEIGHT);
        cmdTestbt.setBounds(110,200,150,Common.HEIGHT);
        cmdTestOneBT.setBounds(260,200,100,Common.HEIGHT);
        limitsSetting.setBounds(200,250,150,Common.HEIGHT);

        exit.setBounds(this.getWidth()-60-5, 5, 60, 20);

        commType.setBounds(10,55,150, Common.HEIGHT);


        StyleUtils.setBtnBg(startstopbt);
        StyleUtils.setBtnBg(groupOperation);
        StyleUtils.setBtnBg(update);
        StyleUtils.setBtnBg(showAllDevice);
        StyleUtils.setBtnBg(sunTracking);
        StyleUtils.setBtnBg(setting);
        StyleUtils.setBtnBg(schedule);
        StyleUtils.setBtnBg(upComBox);
        StyleUtils.setBtnBg(upBt);
        StyleUtils.setBtnBg(downbt);
        StyleUtils.setBtnBg(stopButton);
        StyleUtils.setBtnBg(cmdTestbt);
        StyleUtils.setBtnBg(cmdTestOneBT);
        StyleUtils.setBtnBg(limitsSetting);
        StyleUtils.setBtnBg(exit);

        add(cmdTextEdit);

        add(startstopbt);
        add(groupOperation);
        add(update);
        add(sunTracking);
        add(setting);
        add(showAllDevice);
        add(schedule);

        add(upBt);
        add(downbt);
        add(stopButton);
        add(comBox);
        add(upComBox);

        add(devBox);
        add(versionLabel);
        add(draperID);
        add(address);

        add(cmdTestbt);
        add(cmdTextEdit);
        add(cmdTestOneBT);

        add(limitsSetting);

        add(exit);

        add(commType);

        startstopbt.addActionListener(this);
        upComBox.addActionListener(this);
        sunTracking.addActionListener(this);
        setting.addActionListener(this);
        showAllDevice.addActionListener(this);
        schedule.addActionListener(this);

        upBt.addActionListener(this);
        stopButton.addActionListener(this);
        downbt.addActionListener(this);
        cmdTestbt.addActionListener(this);
        cmdTestOneBT.addActionListener(this);

        limitsSetting.addActionListener(this);
        groupOperation.addActionListener(this);
        update.addActionListener(this);

        exit.addActionListener(this);

        setVisible(true);

        addWindowListener(new MyWindowEventHandle());
        listPort();
        listCommType();
    }

    private void listCommType() {
        commType.addItem("MSTP");
        commType.addItem("IP");
        commType.addItem("UART");
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(startstopbt.equals(e.getSource())){
            mBoxLayoutCasePresenter.startstopbt();
        }else if(upComBox.equals(e.getSource())){
            listPort();
        }else if(upBt.equals(e.getSource())){
            mBoxLayoutCasePresenter.upBt();
        }else if(stopButton.equals(e.getSource())){
            mBoxLayoutCasePresenter.stopButton();
        }else if(downbt.equals(e.getSource())){
            mBoxLayoutCasePresenter.downbt();
        }else if(cmdTestbt.equals(e.getSource())){
            mBoxLayoutCasePresenter.cmdTestbt();
        }else if(cmdTestOneBT.equals(e.getSource())){
            mBoxLayoutCasePresenter.cmdTestOneBT();
        }else if(limitsSetting.equals(e.getSource())){
//            mBoxLayoutCasePresenter.limitsSetting();
            LimitsAndStops limitsAndStops = new LimitsAndStops();
            limitsAndStops.setVisible(true);
        }else if(groupOperation.equals(e.getSource())){
            GroupOperation groupOperation = new GroupOperation();
            groupOperation.setLocationRelativeTo(null);
            groupOperation.setVisible(true);
        }else if(update.equals(e.getSource())){
            Update update1 = new Update();
//            update1.setLocationRelativeTo(null);
            update1.setVisible(true);
        }
//        else if(database.equals(e.getSource())){
//            DataBase dataBase = new DataBase();
//            dataBase.setLocationRelativeTo(null);
//            dataBase.setVisible(true);
//        }
        else if(sunTracking.equals(e.getSource())){
            SuntrackingView suntrackingView = new SuntrackingView();
            suntrackingView.setLocationRelativeTo(null);
            suntrackingView.setVisible(true);
        } else if(setting.equals(e.getSource())){
            SettingView settingView = new SettingView();
            settingView.setLocationRelativeTo(null);
            settingView.setVisible(true);
        }else if( schedule.equals(e.getSource())){
//            ScheduleView schedule = new ScheduleView();
//            schedule.setLocationRelativeTo(null);
//            schedule.setVisible(true);
            ScheduleOnly schedule = new ScheduleOnly();
            schedule.setLocationRelativeTo(null);
            schedule.setVisible(true);
        }else if(showAllDevice.equals(e.getSource())){
            ShowAllDevice showAllDevice = new ShowAllDevice();
            showAllDevice.setLocationRelativeTo(null);
            showAllDevice.setVisible(true);
        }else if(exit.equals(e.getSource())){
            try {
                Public.createShutdownScriptFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
//            System.exit(1);
        }
    }

    @Override
    public void AddItem(RemoteDevice d) {
        devBox.addItem(d);
    }

    @Override
    public void updatedevBox(String text, boolean isRemove) {
        startstopbt.setText(text);
        if(isRemove){
            devBox.removeAllItems();
        }
    }

    /**
     * 更新COM端口
     */
    public void listPort(){
        comBox.removeAllItems();
        LinkedList<String> listPort= ComPortutils.listPort();
        for (String port :listPort) {
            comBox.addItem(port);
        }
        if(mBoxLayoutCasePresenter.getRunningState()){
            mBoxLayoutCasePresenter.startstopbt();
        }
    }

    @Override
    public void updateVersionLabel(String text) {
        versionLabel.setText(text);
    }

    @Override
    public String getCmdTextEdit() {
        return cmdTextEdit.getText().trim();
    }

    @Override
    public String getComBoxSelectedItem() {
        return (String)comBox.getSelectedItem();
    }

    @Override
    public String getCommType() {
        return (String)commType.getSelectedItem();
    }

    @Override
    public RemoteDevice getdevBoxSelectedItem() {
        return (RemoteDevice)devBox.getSelectedItem();
    }
}
