package limitsAndStops.view;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import common.Common;
import limitsAndStops.presenter.LimitsAndStopsPresenter;
import limitsAndStops.presenter.LimitsAndStopsPresenterImpl;
import model.DraperInformation;
import rx.functions.Action1;
import util.Draper;
import util.RxBus;
import util.StyleUtils;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class LimitsAndStops extends JFrame implements ActionListener,LimitsAndStopsView{

    private final LimitsAndStopsPresenter mLimitsAndStopsPresenter;
    public JComboBox devBox=new JComboBox();
    public JButton refresh =new JButton("refresh");

    public JButton extended =new JButton("extended");
    public JButton retracted =new JButton("retracted");
    public JButton JOpen =new JButton("JOpen");
    public JButton JClose =new JButton("JClose");
    public JButton stop =new JButton("stop");
    public JButton upperLimit =new JButton("upperLimit");
    public JButton lowerLimit =new JButton("lowerLimit");
    public JButton preStop =new JButton("preStop");
    public JButton nextStop =new JButton("nextStop");
    public JButton addStop =new JButton("addStop");
    public JButton remoteStop =new JButton("remoteStop");
    public JButton remoteAllStop =new JButton("remoteAllStop");
    public JButton reverse =new JButton("reverse");

    JLabel direction =new JLabel("Direction");
    JLabel curPosition =new JLabel("CurPosition");
    JLabel upLimit =new JLabel("UpLimit");
    JLabel lowLimit =new JLabel("LowerLimit");
    JLabel stops =new JLabel("Stops");
    JLabel directionLabel =new JLabel("");
    JLabel curPositionLabel =new JLabel("");
    JLabel upLimitLabel =new JLabel("");
    JLabel lowLimitLabel =new JLabel("");
    JList stopList =new JList();
    JScrollPane deviceSP = new JScrollPane(stopList);

    private final Thread thread;
    private JTextField jf1 = new JTextField();
    private JTextField jf2 = new JTextField();
    private JTextField jf3 = new JTextField();
    private JTextField jf4 = new JTextField();
    private JTextField jfTime = new JTextField();
    private JTextField jfInterval = new JTextField();
    private JButton jbStart = new JButton("开始");
    private JLabel jlCount = new JLabel("次数");

    private JTextField jfId = new JTextField();
    private JTextField jfDistance = new JTextField();
    private JTextField jfStep = new JTextField();
    private JButton jbConfiguration = new JButton("配置");
    private JButton jbAutoCfg = new JButton("自动");



    public LimitsAndStops( ) throws HeadlessException {
        setTitle("LimitsAndStops");
        setLayout(null);
        setSize(Common.SCREEN_WEIGHT,Common.SCREEN_HEIGHT);
//        setSize(700,400);
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

        mLimitsAndStopsPresenter = new LimitsAndStopsPresenterImpl(this);

        devBox.setBounds(10,120,400,20);
        refresh.setBounds(290,100,100,20);

        extended.setBounds(10,10,100,20);
        stop.setBounds(10,30,100,20);
        retracted.setBounds(10,50,100,20);
        JOpen.setBounds(110,10,100,20);
        JClose.setBounds(110,50,100,20);
        upperLimit.setBounds(220,10,100,20);
        lowerLimit.setBounds(220,50,100,20);
        preStop.setBounds(400,10,100,20);
        nextStop.setBounds(400,50,100,20);
        addStop.setBounds(520,10,150,20);
        remoteStop.setBounds(520,30,150,20);
        remoteAllStop.setBounds(520,50,150,20);
        reverse.setBounds(100,90,100,20);

        direction.setBounds(10,140,100,20);
        curPosition.setBounds(10,160,100,20);
        upLimit.setBounds(10,180,100,20);
        lowLimit.setBounds(10,200,100,20);
        stops.setBounds(10,220,100,20);
        directionLabel.setBounds(120,140,100,20);
        curPositionLabel.setBounds(120,160,100,20);
        upLimitLabel.setBounds(120,180,100,20);
        lowLimitLabel.setBounds(120,200,100,20);
        deviceSP.setBounds(120,220,100,100);

        jf1.setBounds(250, 220, 200, 20);
        jf2.setBounds(250, 240, 200, 20);
        jf3.setBounds(250, 260, 200, 20);
        jf4.setBounds(250, 280, 200, 20);
        jfTime.setBounds(470, 220, 100, 20);
        jfInterval.setBounds(470, 240, 100, 20);
        jbStart.setBounds(470, 260, 100, 20);
        jlCount.setBounds(470, 280, 100, 20);

        jfId.setBounds(450,100,100,20);
        jfDistance.setBounds(450,120,100,20);
        jfStep.setBounds(450,140,100,20);
        jbConfiguration.setBounds(450,160,100,20);
        jbAutoCfg.setBounds(550,160,50,20);

        StyleUtils.setBtnBg(extended);
        StyleUtils.setBtnBg(stop);
        StyleUtils.setBtnBg(JOpen);
        StyleUtils.setBtnBg(JClose);
        StyleUtils.setBtnBg(retracted);
        StyleUtils.setBtnBg(upperLimit);
        StyleUtils.setBtnBg(lowerLimit);
        StyleUtils.setBtnBg(preStop);
        StyleUtils.setBtnBg(nextStop);
        StyleUtils.setBtnBg(addStop);
        StyleUtils.setBtnBg(remoteStop);
        StyleUtils.setBtnBg(remoteAllStop);
        StyleUtils.setBtnBg(reverse);
        StyleUtils.setBtnBg(refresh);
        StyleUtils.setBtnBg(jbConfiguration);
        StyleUtils.setBtnBg(jbAutoCfg);
        StyleUtils.setBtnBg(jbStart);

        add(devBox);
        add(refresh);

        add(extended);
        add(stop);
        add(JOpen);
        add(JClose);
        add(retracted);
        add(upperLimit);
        add(lowerLimit);
        add(preStop);
        add(nextStop);
        add(addStop);
        add(remoteStop);
        add(remoteAllStop);
        add(reverse);

        add(direction);
        add(curPosition);
        add(upLimit);
        add(lowLimit);
        add(stops);
        add(directionLabel);
        add(curPositionLabel);
        add(upLimitLabel);
        add(lowLimitLabel);
        add(deviceSP);


        add(jf1);
        add(jf2);
        add(jf3);
        add(jf4);
        add(jfTime);
        add(jfInterval);
        add(jbStart);
        add(jlCount);

        add(jfId);
        add(jfDistance);
        add(jfStep);
        add(jbConfiguration);
        add(jbAutoCfg);

        extended.addActionListener(this);
        stop.addActionListener(this);
        retracted.addActionListener(this);
        JOpen.addActionListener(this);
        JClose.addActionListener(this);
        upperLimit.addActionListener(this);
        lowerLimit.addActionListener(this);
        preStop.addActionListener(this);
        nextStop.addActionListener(this);
        addStop.addActionListener(this);
        remoteStop.addActionListener(this);
        remoteAllStop.addActionListener(this);
        reverse.addActionListener(this);

        devBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==ItemEvent.SELECTED){
                    mLimitsAndStopsPresenter.getDraperInformation();
                }
            }
        });
        setLocationRelativeTo(null);

        RxBus.getDefault().toObservable(DraperInformation.class)
                .subscribe(new Action1<DraperInformation>() {
                    @Override
                    public void call(DraperInformation draperInformation) {
                        updateDraperInfomation(draperInformation);
                    }
                });

        jbStart.addActionListener(this);
        refresh.addActionListener(this);
        jbConfiguration.addActionListener(this);
        jbAutoCfg.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isRunning=false;
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                send();
            }
        });
        thread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(extended.equals(source)){
            mLimitsAndStopsPresenter.extended();
        }else if(stop.equals(source)){
            mLimitsAndStopsPresenter.stop();
        }else if(retracted.equals(source)){
            mLimitsAndStopsPresenter.retracted();
        }else if(JOpen.equals(source)){
            mLimitsAndStopsPresenter.JOpen();
        }else if(JClose.equals(source)){
            mLimitsAndStopsPresenter.JClose();
        }else if(upperLimit.equals(source)){
            mLimitsAndStopsPresenter.upperLimit();
        }else if(lowerLimit.equals(source)){
            mLimitsAndStopsPresenter.lowerLimit();
        }else if(preStop.equals(source)){
            mLimitsAndStopsPresenter.preStop();
        }else if(nextStop.equals(source)){
            mLimitsAndStopsPresenter.nextStop();
        }else if(addStop.equals(source)){
            mLimitsAndStopsPresenter.addStop();
        }else if(remoteStop.equals(source)){
            mLimitsAndStopsPresenter.remoteStop();
        } else if(reverse.equals(source)){
            mLimitsAndStopsPresenter.remoteStop();
        }else if(remoteStop.equals(source)){
            mLimitsAndStopsPresenter.remoteStop();
        }else if (jbStart.equals(e.getSource())) {
            if (jbStart.getText().equals("开始")) {
                jbStart.setText("暂停");
                start();
            } else {
                jbStart.setText("开始");
                stop();
            }
        }else if(refresh.equals(e.getSource())){
            mLimitsAndStopsPresenter.getDraperInformation();
        }else if(jbConfiguration.equals(e.getSource())){
            int id = Integer.valueOf(jfId.getText().toString().trim());
            int distance = Integer.valueOf(jfDistance.getText().toString().trim());
            //int step = Integer.valueOf(jfStep.getText().toString().trim());
            mLimitsAndStopsPresenter.configuration(id, distance,0);
            //mLimitsAndStopsPresenter.configuration(0, distance,0);
        }else if(jbAutoCfg.equals(e.getSource())){
            isAutoCfg=!isAutoCfg;
            if(isAutoCfg){
                cfgCount=0;
                mLimitsAndStopsPresenter.clearCfgCount();
                timer.start();
            }else {
                timer.stop();
            }
        }
    }

    @Override
    public int getDistance() {
        return Integer.valueOf(jfDistance.getText().toString().trim());
    }

    private boolean isAutoCfg=false;
    private int cfgCount=0;
    Timer timer = new Timer(3000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cfgCount++;
            int id = Integer.valueOf(jfId.getText().toString().trim());
            Random random = new Random();
            int i = random.nextInt(1000);
            int distance =1000+i;
            jfDistance.setText(""+distance);
            mLimitsAndStopsPresenter.configuration(id, distance,0);
            try {
                Thread.sleep(1000);
                mLimitsAndStopsPresenter.saveRemoteDeviceCfg();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    });

    @Override
    public RemoteDevice getSelectedItem() {
        return (RemoteDevice) devBox.getSelectedItem();
    }

    @Override
    public void updateDevBox(List<RemoteDevice> remoteDevices) {
        devBox.removeAll();
        for(RemoteDevice remoteDevice:remoteDevices){
            devBox.addItem(remoteDevice);
        }
    }

    @Override
    public boolean getRunningState() {
        return isLoop;
    }

    public void updateDraperInfomation(DraperInformation draperInformation){
        directionLabel.setText(""+draperInformation.getReverse());
        curPositionLabel.setText(""+draperInformation.getCurPosition());
        upLimitLabel.setText(""+draperInformation.getUpperLimit());
        lowLimitLabel.setText(""+draperInformation.getLowerLimit());
        List<SignedInteger> stop = draperInformation.getStopList();
        SignedInteger[] drapers=new SignedInteger[stop.size()];
        for (int i = 0; i < stop.size(); i++) {
            drapers[i]=stop.get(i);
        }
        stopList.setListData(drapers);
//        List<Integer> a=new ArrayList<>();
//        a.add(2);
//        a.add(2);
//        a.add(2);
//        stopList.addAll(a);
    }

    private void start() {
//        time.start();
        count=0;
        time = Integer.valueOf(jfTime.getText().toString().trim());
        isLoop = true;
    }

    private void stop() {
//        time.stop();
        isLoop = false;
    }

    boolean isLoop = false;
    boolean isRunning = true;
    int time=0;
    int count=0;

    /**
     * 循环发送指令
     */
    private void send() {
        while (isRunning) {
            try {
//            System.out.println("-------isLoop");
                Thread.sleep(10);
                if (isLoop) {
                    count++;
                    jlCount.setText(""+count);
//            byte[] byte1 = Byte2IntUtils.hexStringToBytes(jf1.getText().toString().trim());
                    Integer cmd1 = Integer.valueOf(jf1.getText().toString().trim());
                    Integer cmd2 = Integer.valueOf(jf2.getText().toString().trim());
                    Integer cmd3 = Integer.valueOf(jf3.getText().toString().trim());
                    Integer cmd4 = Integer.valueOf(jf4.getText().toString().trim());

                    send(cmd1);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    send(cmd2);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    send(cmd3);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    send(cmd4);
                    Thread.sleep(Integer.valueOf(jfInterval.getText().toString().trim()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void send(int cmd) throws Exception {
        Draper.sendCmd(cmd);
        mLimitsAndStopsPresenter.save(cmd);
        switch (cmd){
            case 1:
                mLimitsAndStopsPresenter.saveRemoteDeviceInfomation();
                break;
        }
    }
}
