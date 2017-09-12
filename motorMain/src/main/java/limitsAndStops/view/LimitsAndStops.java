package limitsAndStops.view;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import limitsAndStops.presenter.LimitsAndStopsPresenter;
import limitsAndStops.presenter.LimitsAndStopsPresenterImpl;
import model.DraperInformation;
import rx.functions.Action1;
import util.RxBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class LimitsAndStops extends JFrame implements ActionListener,LimitsAndStopsView{

    private final LimitsAndStopsPresenter mLimitsAndStopsPresenter;
    public JComboBox devBox=new JComboBox();

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



    public LimitsAndStops( ) throws HeadlessException {
        setTitle("LimitsAndStops");
        setLayout(null);
        mLimitsAndStopsPresenter = new LimitsAndStopsPresenterImpl(this);
        setSize(700,400);
        devBox.setBounds(10,120,400,20);

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

        add(devBox);
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
        }
    }

    @Override
    public RemoteDevice getSelectedItem() {
        return (RemoteDevice) devBox.getSelectedItem();
    }

    @Override
    public void updateDevBox(List<RemoteDevice> remoteDevices) {
        for(RemoteDevice remoteDevice:remoteDevices){
            devBox.addItem(remoteDevice);
        }
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
}
