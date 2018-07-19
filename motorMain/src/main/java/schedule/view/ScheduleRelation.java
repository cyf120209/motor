package schedule.view;

import entity.Schedule;
import entity.ShadeGroup;
import groupOperation.IGroupCallback;
import groupOperation.view.GroupOperation;
import common.Common;
import schedule.presenter.ScheduleRelationPresenterImpl;
import model.DeviceGroup;
import util.Draper;
import util.MyLocalDevice;
import util.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

//TODO 添加滚动条
public class ScheduleRelation extends JFrame implements IScheduleRelation,ActionListener{

    protected JList jlScheduleList;
    private ScheduleRelationPresenterImpl mScheduleRelationPresenter;
    Panel existedGroup = new Panel();
    JScrollPane jspGroup=new JScrollPane(existedGroup, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JButton btnEditGroup =new JButton("edit group");
    JButton jbIdentify=new JButton("identify select");
    JButton jbAdd=new JButton("save");

    private List<DeviceGroup> deviceGroupList=new ArrayList<>();


    /**
     * 选中schedule的行号
     */
    private int scheduleIndex;
    private List<Schedule> mScheduleList;

    public ScheduleRelation() throws HeadlessException {
        setTitle("ScheduleRelation");
        setSize(Common.SCREEN_WEIGHT,Common.SCREEN_HEIGHT);

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

        jspGroup.setBounds(300, 10, 200, 300);

        jbIdentify.setBounds(400,350,120,30);
        btnEditGroup.setBounds(300,350,100,30);
        jbAdd.setBounds(520,350,100,30);

        StyleUtils.setBtnBg(btnEditGroup);
        StyleUtils.setBtnBg(jbIdentify);
        add(jspGroup);
        add(btnEditGroup);
        add(jbIdentify);
        add(jbAdd);

        initView();

        btnEditGroup.addActionListener(this);
        jbIdentify.addActionListener(this);
        jbAdd.addActionListener(this);

        mScheduleRelationPresenter = new ScheduleRelationPresenterImpl(this);

        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));

    }

    private void initView() {
        // schedule 列表
        jlScheduleList = new JList();
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        jlScheduleList.setCellRenderer(renderer);
        jlScheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        CheckListener lst = new CheckListener(this);
        jlScheduleList.addMouseListener(lst);
        jlScheduleList.addKeyListener(lst);

        JScrollPane ps = new JScrollPane();
        ps.setBounds(10,10,200,300);
        ps.getViewport().add(jlScheduleList);
        add(ps);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnEditGroup)){
            editGroup();
        }else if(e.getSource().equals(jbIdentify)){
            identify();
        }else if(e.getSource().equals(jbAdd)){
            addRelation();
        }
    }

    private void addRelation() {
        addScheduleGroup();
    }

    private void identify() {
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

    private void editGroup() {
        GroupOperation groupOperation = new GroupOperation(new IGroupCallback() {
            @Override
            public void updateExistedGroup(Object[] arr) {
                ScheduleRelation.this.updateExistedGroup(arr);
            }
        });
        groupOperation.setLocationRelativeTo(null);
        groupOperation.setVisible(true);
    }


    JCheckBox[] jcbGroup;
    List<Integer> idGroupList = new ArrayList<>();

    //更新组列表
    public void updateExistedGroup(Object[] groupList) {
        deviceGroupList.clear();
        existedGroup.removeAll();
        Map<Integer, Map<Integer, List<Integer>>> relationMap = MyLocalDevice.mRemoteUtils.getRelationMap();
        for(Object o:groupList){
            Integer deviceId = Integer.valueOf(String.valueOf(o));
            Map<Integer, List<Integer>> groupListMap = relationMap.get(deviceId);
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = groupListMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, List<Integer>> group = iterator.next();
                Integer groupId = group.getKey();
//                List<Integer> shadeList = group.getValue();
//                deviceGroupList.add(new DeviceGroup(deviceId,groupId,shadeList));
                deviceGroupList.add(new DeviceGroup(deviceId,groupId));
            }
        }
        jcbGroup = new JCheckBox[deviceGroupList.size()];
        for (int i = 0; i < deviceGroupList.size(); i++) {
            jcbGroup[i] = new JCheckBox(deviceGroupList.get(i).toString());
//            idGroupList.add(Integer.valueOf(String.valueOf(arr[i])));
//            jcbGroup[i].addActionListener(jcbGroupListener);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            existedGroup.add(jcbGroup[i]);
        }
//        existedGroup.invalidate();
        existedGroup.validate();
//        existedGroup.repaint();
    }

    @Override
    public DeviceGroup getDeviceGroup() {
        return null;
    }

    JCheckBox[] jcbSchedule;

    @Override
    public void showSchedule(List<Schedule> scheduleList) {
        mScheduleList=scheduleList;
        ScheduleData[] scheduleData = new ScheduleData[scheduleList.size()];
        for (int i = 0; i < scheduleList.size(); i++) {
            Schedule schedule = scheduleList.get(i);
            scheduleData[i]=new ScheduleData(schedule.getScheduleName());
        }
        jlScheduleList.setListData(scheduleData);

    }

    int toAddIndex=-1;
    public void selectedIndex(int index){
        toAddIndex=index;
        Schedule scd = mScheduleList.get(index);
        Schedule schedule = mScheduleRelationPresenter.getGroupListByScheduleId(scd.getId());
        List<ShadeGroup> shadeGroupList = schedule.getShadeGroups();
        showCheckedGroup(shadeGroupList);

    }

    private void showCheckedGroup(List<ShadeGroup> shadeGroupList) {
        List<Integer> checkedList=new ArrayList<>();
        for(ShadeGroup shadeGroup:shadeGroupList){
            for (int i = 0; i < deviceGroupList.size(); i++) {
                DeviceGroup deviceGroup = deviceGroupList.get(i);
                if(shadeGroup.getGroupId().intValue()==deviceGroup.getGroupId().intValue() &&
                        shadeGroup.getDeviceId().intValue() == deviceGroup.getDeviceId().intValue()){
                    checkedList.add(new Integer(i));
                }
            }
        }
        for (int i = 0; i < jcbGroup.length; i++) {
            if(checkedList.contains(new Integer(i))){
                jcbGroup[i].setSelected(true);
            }else {
                jcbGroup[i].setSelected(false);
            }
        }
//        if(checkedList.size()>0){
//            for(Integer i:checkedList){
//            }
//        }
        existedGroup.validate();
    }

    private void addScheduleGroup(){
        if(toAddIndex==-1 || jcbGroup==null){
            return;
        }
        List<DeviceGroup> toAddGroupList=new ArrayList<>();
        List<DeviceGroup> toDelGroupList=new ArrayList<>();
        for (int i = 0; i < jcbGroup.length; i++) {
            if(jcbGroup[i].isSelected()){
                DeviceGroup deviceGroup = deviceGroupList.get(i);
                toAddGroupList.add(deviceGroup);
            }else {
                DeviceGroup deviceGroup = deviceGroupList.get(i);
                toDelGroupList.add(deviceGroup);
            }
        }
        mScheduleRelationPresenter.saveScheduleGroup(toAddIndex,toAddGroupList,toDelGroupList);
    }
}
