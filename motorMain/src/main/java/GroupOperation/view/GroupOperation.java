package GroupOperation.view;

import GroupOperation.presenter.GroupOperationPresenter;
import GroupOperation.presenter.GroupOperationPresenterImpl;
import com.serotonin.bacnet4j.RemoteDevice;
import common.Common;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by lenovo on 2017/1/19.
 */
public class GroupOperation extends JFrame implements ActionListener,GroupOperationView,ListSelectionListener{

    private GroupOperationPresenter mGroupOperationPresenter;
    public JLabel groupText=new JLabel("Group Num:");
    public JLabel deviceText=new JLabel("Device Num:");
    public JTextField groupEdit=new JTextField(10);
    public JTextField deviceEdit=new JTextField(10);

    public JButton AnnounceSubbutton=new JButton("Announce");
    public JComboBox devBox=new JComboBox();
    public JButton addSelGroup=new JButton("AddSelGroup");
    public JButton delSelGroup=new JButton("DelSelGroup");
    public JButton addAllGroup=new JButton("AddALLGroup");
    public JButton delAllGroup=new JButton("DelALLGroup");

    public JButton upBt=new JButton("up");
    public JButton downbt=new JButton("down");
    public JButton stopButton=new JButton("stop");

    public JList deviceList = new JList();
    JScrollPane deviceSP = new JScrollPane(deviceList);
    public JList groupList = new JList();
    JScrollPane groupSP = new JScrollPane(groupList);
    public JList draperList = new JList();
    JScrollPane draperSP = new JScrollPane(draperList);

    public GroupOperation() throws HeadlessException {

        mGroupOperationPresenter = new GroupOperationPresenterImpl(this);
        setTitle("GroupOperation");
        setLayout(null);
        setSize(450,400);

        AnnounceSubbutton.setBounds(10,10,100,20 );
        devBox.setBounds(130,10,300,20);
        groupText.setBounds(10,40,100,20 );
        groupEdit.setBounds(100,40,100,20);
        deviceText.setBounds(10,60,100,20 );
        deviceEdit.setBounds(100,60,100,20);
        addSelGroup.setBounds(10,100,120,20 );
        addAllGroup.setBounds(10,120,120,20 );
        delSelGroup.setBounds(130,100,120,20 );
        delAllGroup.setBounds(130,120,120,20 );

        deviceSP.setBounds(10,150,100,200);
        groupSP.setBounds(120,150,100,200);
        draperSP.setBounds(240,150,100,200);

        upBt.setBounds(new Rectangle(270, 40, 100, Common.HEIGHT));
        downbt.setBounds(new Rectangle(270, 60, 100, Common.HEIGHT));
        stopButton.setBounds(new Rectangle(270, 80, 100, Common.HEIGHT));

        add(AnnounceSubbutton);
        add(devBox);
        add(addAllGroup);
        add(delAllGroup);
        add(addSelGroup);
        add(delSelGroup);
        add(groupText);
        add(deviceText);
        add(groupEdit);
        add(deviceEdit);

        add(deviceSP);
        add(groupSP);
        add(draperSP);

        add(upBt);
        add(downbt);
        add(stopButton);

        AnnounceSubbutton.addActionListener(this);
        addSelGroup.addActionListener(this);
        addAllGroup.addActionListener(this);
        delSelGroup.addActionListener(this);
        delAllGroup.addActionListener(this);

        upBt.addActionListener(this);
        stopButton.addActionListener(this);
        downbt.addActionListener(this);

        deviceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean valueIsAdjusting = e.getValueIsAdjusting();
                if(!valueIsAdjusting){
                    return;
                }
                JList deviceSource = (JList)e.getSource();
                int selectedIndex = deviceSource.getSelectedIndex();
                String[] groups = mGroupOperationPresenter.getGroups(selectedIndex);
                updateGroup(groups);
                deviceEdit.setText(""+deviceList.getSelectedValue());

            }
        });
        groupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean valueIsAdjusting = e.getValueIsAdjusting();
                if(!valueIsAdjusting){
                    return;
                }
                System.out.println(""+valueIsAdjusting);
                JList groupSource = (JList)e.getSource();
                int selectedIndex = groupSource.getSelectedIndex();
                String[] drapers = mGroupOperationPresenter.getDrapers(selectedIndex);
                updateDraperList(drapers);
                groupEdit.setText(""+groupList.getSelectedValue());
            }
        });

        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mGroupOperationPresenter.cancelListener();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(AnnounceSubbutton.equals(e.getSource())){
            mGroupOperationPresenter.AnnounceSubbutton();
        }else if(addSelGroup.equals(e.getSource())){
            mGroupOperationPresenter.addSelGroup();
        }else if(addAllGroup.equals(e.getSource())){
            mGroupOperationPresenter.addAllGroup();
        }else if(delSelGroup.equals(e.getSource())){
            mGroupOperationPresenter.delSelGroup();
        }else if(delAllGroup.equals(e.getSource())){
            mGroupOperationPresenter.delAllGroup();
        }else if(upBt.equals(e.getSource())){
            mGroupOperationPresenter.upBt();
        }else if(stopButton.equals(e.getSource())){
            mGroupOperationPresenter.stopButton();
        }else if(downbt.equals(e.getSource())){
            mGroupOperationPresenter.downbt();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(deviceList.equals(e.getSource())){
            JList deviceSource = (JList)e.getSource();
            int selectedIndex = deviceSource.getSelectedIndex();
            String[] groups = mGroupOperationPresenter.getGroups(selectedIndex);
            mGroupOperationPresenter.AnnounceSubbutton();
        }else if(groupList.equals(e.getSource())){
            mGroupOperationPresenter.addSelGroup();
        }
    }

    public void updateDraperList(String[] drapers) {
        draperList.setListData(drapers);
    }

    @Override
    public void updateDraper(String[] drapers) {
        devBox.removeAllItems();
        for (String s:drapers){
            devBox.addItem(s);
        }
    }

    @Override
    public void updateDevice(Object[] arr) {
        deviceList.setListData(arr);
    }

    @Override
    public void updateGroup(Object[] arr) {
        groupList.setListData(arr);
    }

    @Override
    public int getdevBoxSelectedItem() {
        return devBox.getSelectedIndex();
    }

    @Override
    public int getdevBoxSelectedIndex() {
        return devBox.getSelectedIndex();
    }

    @Override
    public int getDeviceNum(){
        return Integer.parseInt(deviceEdit.getText().trim());
    }

    @Override
    public int getGroupNum(){
        return Integer.parseInt(groupEdit.getText().trim());

    }

}
