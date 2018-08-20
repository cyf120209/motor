package groupOperation.view;

import entity.Device;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/1/19.
 */
public class GroupOperation extends JFrame implements ActionListener,GroupOperationView,ListSelectionListener{

    private GroupOperationPresenter mGroupOperationPresenter;

    public JButton AnnounceSubbutton=new JButton("Announce");

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
    List<DeviceGroup> mDeviceGroupList=new ArrayList<>();

    IGroupCallback callback;
    private int selectedIndex=-1;

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

//        devicePanel.setBounds(5, 5, 100, 300);
        groupSP.setBounds(110, 5, 300, 150);

//        add(devicePanel);
        add(groupSP);
        addMotor();

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
                if(callback!=null){
                    callback.updateExistedGroup(list);
                }
            }
        });
    }

    private void initData() {
        groupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedIndex = groupList.getSelectedIndex();
                if(selectedIndex==-1){
                    return;
                }
                mDeviceGroup = mDeviceGroupList.get(selectedIndex);
                List<Integer> shadeList = mDeviceGroup.getShadeList();
                if(shadeList==null || shadeList.size()==0) return;
//                System.out.println("deviceList size: "+shadeList.size());
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

//                System.out.println("checked"+checkedList.size());
                showChecked();
//                int checked = -1;
//                for (int i = 0; i < jcbGroup.length; i++) {
//                    if (e.getSource().equals(jcbGroup[i]) && jcbGroup[i].isSelected()) {
//                        checked=1;
//                    }
//                }
//                if(checked==-1){
//                    Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.mRemoteUtils.getRelationMap();
//                    Map<Integer, List<Integer>> integerListMap = relationMap.get(900900);
//                    deviceGroup=new DeviceGroup(900900,integerListMap.size()+1);
//                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(AnnounceSubbutton.equals(e.getSource())){
            mGroupOperationPresenter.AnnounceSubbutton();
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
        if(selectedIndex==-1){
            boolean isSet= false;
            for (int i = 1; i < mDeviceGroupList.size()+1; i++) {
                DeviceGroup deviceGroup = mDeviceGroupList.get(i-1);
                Integer groupId = deviceGroup.getGroupId();
                if(groupId.intValue()!=i){
                    mDeviceGroup.setGroupId(i);
                    isSet=true;
                    break;
                }
            }
            if(!isSet){
                mDeviceGroup.setGroupId(mDeviceGroupList.size()+1);
            }
        }
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        boolean success = true;
        String errorId = "";
        for (int i = 0; i < jcbMotor.length; i++) {
            Integer id = idMotorList.get(i);
            int groupID = mDeviceGroup.getGroupId();
            try {
                if (jcbMotor[i].isSelected()) {
                    Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(id), false, mDeviceGroup.getDeviceId(), groupID);
                } else {
                    Draper.sendGroupSubscriptionToSelect(remoteDeviceMap.get(id), true, mDeviceGroup.getDeviceId(), groupID);
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
//                    Thread.sleep(1500);
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
    DeviceGroup mDeviceGroup=new DeviceGroup(900900,1);

    ActionListener jcbGroupListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < jcbGroup.length; i++) {
                if (e.getSource().equals(jcbGroup[i]) && jcbGroup[i].isSelected()) {
                    mDeviceGroup = mDeviceGroupList.get(i);
                    List<Integer> shadeList = mDeviceGroup.getShadeList();

                } else {
                    jcbGroup[i].setSelected(false);
                }
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

    /**
     *  只显示自己（900900）的组
     * @param deviceList
     */
    @Override
    public void updateExistedGroup(Object[] deviceList) {
        mDeviceGroupList.clear();
        this.list=deviceList;
        Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.mRemoteUtils.getRelationMap();
        for(Object o:deviceList){
            Integer deviceId = Integer.valueOf(String.valueOf(o));
//            if(deviceId.intValue()==900900) {
                Map<Integer, List<Integer>> groupListMap = relationMap.get(deviceId);
                Iterator<Map.Entry<Integer, List<Integer>>> iterator = groupListMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, List<Integer>> group = iterator.next();
                    Integer groupId = group.getKey();
                    List<Integer> shadeList = group.getValue();
                    mDeviceGroupList.add(new DeviceGroup(deviceId, groupId, shadeList));
                }
//            }
        }
        showGroupList(mDeviceGroupList);
    }

    /**
     * 只显示自己（900900）的组
     * @param list
     */
    private void showGroupList(List<DeviceGroup> list) {
        String[] groupArr=new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            groupArr[i] = list.get(i).toString();
        }
        updateGroup(groupArr);
    }

    private void showGroupInfo(DeviceGroup dg) {
//        existedGroup.removeAll();
//        JLabel jLabel = new JLabel(dg.toString());
//        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));
//        existedGroup.add(jLabel);
//        existedGroup.validate();
//        existedGroup.repaint();
//        String[] groupArr=new String[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            groupArr[i] = list.get(i).toString();
//        }
//        updateGroup(groupArr);
//        showMotor();
    }

    @Override
    public DeviceGroup getDeviceGroup() {
        return mDeviceGroup;
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
    public void updateDevice(Object[] arr) {
        deviceList.setListData(arr);
    }

    @Override
    public void updateGroup(Object[] arr) {
        groupList.setListData(arr);
    }

    public void showError(String str) {
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSuccess(String str) {
        JOptionPane.showMessageDialog(null, str, "success", JOptionPane.PLAIN_MESSAGE);
    }

}
