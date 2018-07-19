package groupOperation.view;

import groupOperation.IGroupCallback;
import groupOperation.presenter.GroupOperationPresenter;
import groupOperation.presenter.GroupOperationPresenterImpl;
import com.serotonin.bacnet4j.RemoteDevice;
import model.DeviceGroup;
import util.Draper;
import util.MyLocalDevice;
import util.Public;
import util.StyleUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


    JCheckBox[] jcbGroup;
    java.util.List<Integer> idGroupList = new ArrayList<>();
    List<DeviceGroup> deviceGroupList=new ArrayList<>();

    Panel devicePanel = new Panel();

    Panel existedGroup = new Panel();

    IGroupCallback callback;

    public GroupOperation(IGroupCallback callback) throws HeadlessException{
        this.callback=callback;
        initView();
    }

    public GroupOperation() throws HeadlessException {
        initView();
    }

    private void initView(){
        mGroupOperationPresenter = new GroupOperationPresenterImpl(this);
        setTitle("groupOperation");
        setLayout(null);
        setSize(600,400);

        devicePanel.setBounds(5, 5, 100, 300);
        existedGroup.setBounds(110, 5, 300, 150);

        add(devicePanel);
        add(existedGroup);
        addMotor();
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("软件部");
        node1.add(new DefaultMutableTreeNode("小花"));
        node1.add(new DefaultMutableTreeNode("小虎"));
        node1.add(new DefaultMutableTreeNode("小龙"));

        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("销售部");
        node2.add(new DefaultMutableTreeNode("小叶"));
        node2.add(new DefaultMutableTreeNode("小雯"));
        node2.add(new DefaultMutableTreeNode("小夏"));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("职员管理");

        top.add(new DefaultMutableTreeNode("总经理"));
        top.add(node1);
        top.add(node2);
        final JTree tree = new JTree(top);
        initData();

        // 把背景图片显示在一个标签里面
        JLabel label = new JLabel(StyleUtils.getFormBg());
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mGroupOperationPresenter.cancelListener();
                if(callback!=null && deviceEdit!=null){
                    callback.updateExistedGroup(list);
                }
            }
        });
    }

    private void initData() {
//        List<Integer> remoteDeviceIDList = MyLocalDevice.mRemoteUtils.getRemoteDeviceIDList();
//        JCheckBox[] cbDeviceList=new JCheckBox[remoteDeviceIDList.size()];

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
        else if (e.getSource().equals(jbAdd)) {
            addGroup();
        } else if (e.getSource().equals(jbIdentify)) {
            identify();
        } else if (e.getSource().equals(jbCancel)) {
            cancel();
        }
    }

    private void addGroup() {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        boolean success = true;
        String errorId = "";
        for (int i = 0; i < jcbMotor.length; i++) {
            Integer id = idMotorList.get(i);
            int groupID = deviceGroup.getGroupId();
            try {
                if (jcbMotor[i].isSelected()) {
                    Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(id), false, deviceGroup.getDeviceId(), groupID);
                } else {
                    Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(id), true, deviceGroup.getDeviceId(), groupID);
                }
                Thread.sleep(500);
            } catch (Exception e1) {
                success = false;
                errorId += id + " ";
                e1.printStackTrace();
            }
        }
        if (success) {
            showSuccess("add to group succeed");
        } else {
            showError("error: " + errorId + "add to group error");
        }
        mGroupOperationPresenter.AnnounceSubbutton();
    }

    private void identify() {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        for (int i = 0; i < jcbMotor.length; i++) {
            if (jcbMotor[i].isSelected()) {
                Integer id = idMotorList.get(i);
                RemoteDevice remoteDevice = remoteDeviceMap.get(id);
                try {
                    Draper.sendCmd(remoteDevice, 1602);
                    Thread.sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cancel() {
        showChecked();
    }


    List<Integer> checkedList = new ArrayList<>();
    DeviceGroup deviceGroup=new DeviceGroup(10000,1);

    ActionListener jcbGroupListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < jcbGroup.length; i++) {
                if (e.getSource().equals(jcbGroup[i]) && jcbGroup[i].isSelected()) {
                    deviceGroup = deviceGroupList.get(i);
                    List<Integer> shadeList = deviceGroup.getShadeList();
                    if(shadeList==null || shadeList.size()==0) return;
                    System.out.println("deviceList size: "+shadeList.size());
                    checkedList.clear();
                    for (int j = 0; j < idMotorList.size(); j++) {
                        Integer id = idMotorList.get(j);
                        for (int k = 0; k < shadeList.size(); k++) {
                            Integer il = shadeList.get(k);
                            if (id.intValue() == il.intValue()) {
                                checkedList.add(id);
                                break;
                            }
                        }
                    }
                    System.out.println("checked"+checkedList.size());
                    showChecked();
                } else {
                    jcbGroup[i].setSelected(false);
                }
            }
            int checked = -1;
            for (int i = 0; i < jcbGroup.length; i++) {
                if (e.getSource().equals(jcbGroup[i]) && jcbGroup[i].isSelected()) {
                    checked=1;
                }
            }
            if(checked==-1){
                Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.mRemoteUtils.getRelationMap();
                Map<Integer, List<Integer>> integerListMap = relationMap.get(10000);
                deviceGroup=new DeviceGroup(10000,integerListMap.size()+1);
            }
        }
    };

    private void showChecked() {
        for (int i = 0; i < idMotorList.size(); i++) {
            jcbMotor[i].setSelected(false);
        }
        for (Integer num : checkedList) {
            jcbMotor[idMotorList.indexOf(num)].setSelected(true);
        }
    }

    Object[] list;
    @Override
    public void updateExistedGroup(Object[] deviceList) {
        deviceGroupList.clear();
        existedGroup.removeAll();
        this.list=deviceList;
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
            jcbGroup[i].addActionListener(jcbGroupListener);
        }
        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));
        for (int i = 0; i < deviceGroupList.size(); i++) {
            existedGroup.add(jcbGroup[i]);
        }
//        existedGroup.invalidate();
        existedGroup.validate();
        existedGroup.repaint();
    }

    @Override
    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    Panel allMotor = new Panel();
    JButton jbCancel = new JButton("Cancel");
    JButton jbIdentify = new JButton("Identify");
    JButton jbAdd = new JButton("Add");

    private void addMotor() {
        Panel operator = new Panel();
        operator.setLayout(null);
        operator.setBounds(110, 156, 300, 200);


        allMotor.setBounds(10, 0, 150, 150);
        jbCancel.setBounds(0, 170, 100, 20);
        jbIdentify.setBounds(100, 170, 100, 20);
        jbAdd.setBounds(200, 170, 100, 20);

        operator.add(allMotor);
        operator.add(jbCancel);
        operator.add(jbIdentify);
        operator.add(jbAdd);

        jbCancel.addActionListener(this);
        jbIdentify.addActionListener(this);
        jbAdd.addActionListener(this);
        add(operator);
        showMotor();
    }

    JCheckBox[] jcbMotor;
    List<Integer> idMotorList = new ArrayList<>();

    private void showMotor() {
        List<RemoteDevice> remoteDeviceList = MyLocalDevice.getRemoteDeviceList();

        for (RemoteDevice remoteDevice : remoteDeviceList) {
            if (Public.matchString(Public.readModelName(remoteDevice), "MC-AC")) {
                idMotorList.add(remoteDevice.getInstanceNumber());
            }
        }
        jcbMotor = new JCheckBox[idMotorList.size()];
        for (int i = 0; i < idMotorList.size(); i++) {
            jcbMotor[i] = new JCheckBox("" + idMotorList.get(i));
        }
        allMotor.setLayout(new BoxLayout(allMotor, BoxLayout.Y_AXIS));
        for (int i = 0; i < idMotorList.size(); i++) {
            allMotor.add(jcbMotor[i]);
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

    public void showError(String str) {
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSuccess(String str) {
        JOptionPane.showMessageDialog(null, str, "success", JOptionPane.PLAIN_MESSAGE);
    }

}
