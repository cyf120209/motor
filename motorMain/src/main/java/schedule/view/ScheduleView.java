package schedule.view;

import dao.ScheduleDao;
import groupOperation.IGroupCallback;
import groupOperation.view.GroupOperation;
import common.Common;
import view.Schedule;
import model.DeviceGroup;
import schedule.presenter.ISchedulePresenter;
import schedule.presenter.SchedulePresenterImpl;
import util.Draper;
import util.MyLocalDevice;
import util.StyleUtils;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

//TODO 添加滚动条
public class ScheduleView extends JFrame implements IScheduleView,ActionListener{

    private final ISchedulePresenter mSchedulePresenter;
    JButton add=new JButton("+");
    JButton delete=new JButton("delete");
    private List<Schedule> scheduleList=new ArrayList<>();
    JPanel jpScheduleContainer=new JPanel();
    JScrollPane jsp=new JScrollPane(jpScheduleContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    Panel existedGroup = new Panel();
    JScrollPane jspGroup=new JScrollPane(existedGroup, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JButton btnEditGroup =new JButton("edit group");
    JButton jbIdentify=new JButton("identify select");

    Map<String,List<Schedule>> weekScheduleList=new HashMap<>();

    private List<DeviceGroup> deviceGroupList=new ArrayList<>();

    public ScheduleView() throws HeadlessException {
        setTitle("Schedule");
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

        jpScheduleContainer.setLayout(null);
//        jpScheduleContainer.setBounds(0,0,200,400);
        jsp.setBounds(0,0,200,400);
        jspGroup.setBounds(300, 0, 300, 400);
        add.setBounds(210,5,50,30);
        delete.setBounds(10,400,100,30);

        jbIdentify.setBounds(610,5,150,30);
        btnEditGroup.setBounds(610,35,150,30);

        StyleUtils.setBtnBg(add);
        StyleUtils.setBtnBg(btnEditGroup);
        StyleUtils.setBtnBg(jbIdentify);
        add(add);
        add(delete);
        add(jspGroup);
        add(jsp);
        add(btnEditGroup);
        add(jbIdentify);

//        addSchedulePanel(new Schedule(1,1,1),0);
//        addSchedulePanel(new Schedule(1,1,1),40);
//        addSchedulePanel(new Schedule(1,1,1),80);
//        addSchedulePanel(new Schedule(1,1,1),120);
//        addSchedulePanel(new Schedule(1,1,1),160);
//        addSchedulePanel(new Schedule(1,1,1),200);
//        addSchedulePanel(new Schedule(1,1,1),240);
//        addSchedulePanel(new Schedule(1,1,1),280);
//        addSchedulePanel(new Schedule(1,1,1),320);
//        addSchedulePanel(new Schedule(1,1,1),360);
//        addSchedulePanel(new Schedule(1,1,1),400);
//        addSchedulePanel(new Schedule(1,1,1),440);
        add.addActionListener(this);
        delete.addActionListener(this);
        btnEditGroup.addActionListener(this);
        jbIdentify.addActionListener(this);

        mSchedulePresenter = new SchedulePresenterImpl(this);
//        jpScheduleContainer.setLayout(new BoxLayout(jpScheduleContainer, BoxLayout.Y_AXIS));
        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));
//        jcbGroup=new JCheckBox[10];
//        for (int i = 0; i < 10; i++) {
//            existedGroup.add(new JCheckBox());
//        }
//        existedGroup.validate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int min = calendar.get(Calendar.MINUTE);
                        int sec = calendar.get(Calendar.SECOND);
                        for(Schedule schedule:scheduleList){
                            if(schedule.getHour()==hour && schedule.getMin()==min && sec==0){
                                List<String> week = schedule.getWeek();
                                if(week==null) continue;
                                for (String w:week) {
                                    int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    if (todayOfWeek == week2Int(w)){
                                        try {
                                            System.out.println("sendCmd");
                                            Draper.sendCmd(schedule.getDeviceId(),schedule.getGroupId(),schedule.getPercent());
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }


                    }
                }).start();
            }
        }).start();

    }

    private int week2Int(String week){
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

    private void addSchedulePanel(Schedule schedule,int y){
        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setBounds(0,y,200,50);
        JLabel time=new JLabel("time "+schedule.getHour()+" : "+schedule.getMin());
        JLabel percent=new JLabel("percent "+schedule.getPercent()+"%");
        JCheckBox cbSchedule=new JCheckBox();
        JLabel week=new JLabel(schedule.getWeekString());
        time.setBounds(5,5,70,20);
        percent.setBounds(80,5,80,20);
        cbSchedule.setBounds(170,5,20,20);
        week.setBounds(5,30,170,20);
        panel.add(time);
        panel.add(percent);
        panel.add(cbSchedule);
        panel.add(week);
        jpScheduleContainer.add(panel);
    }

    private void refresh(){
        jpScheduleContainer.removeAll();
//        JLabel sd = new JLabel();
//        sd.setBounds(0,0,200,100);
//        sd.add(new Schedule(6,10,60).getjPanel());
//        add(sd);
        int y=0;
        for (int i=0;i<scheduleList.size();i++){
            Schedule schedule = scheduleList.get(i);
            addSchedulePanel(schedule,y);
            y=y+45;
        }
        jpScheduleContainer.validate();
        jsp.validate();
    }

    private void showAddSchedule(){
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(this, "Add Schedule", true);
        // 设置对话框的宽高
        dialog.setSize(370, 295);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(this);

        Integer[] h=new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
        Integer[] m=new Integer[60];
        for(int i=0;i<60;i++){
            m[i]=i+1;
        }
        Integer[] p=new Integer[100];
        for(int i=0;i<100;i++){
            p[i]=i+1;
        }

        List<DeviceGroup> list = MyLocalDevice.mRemoteUtils.getDeviceGroupList();
        if(list !=null && list.size()!=0){
            deviceGroupList = list;
        }
        String[] dgList=new String[this.deviceGroupList.size()];
        for(int i = 0; i< this.deviceGroupList.size(); i++){
            DeviceGroup deviceGroup = this.deviceGroupList.get(i);
            dgList[i]=deviceGroup.toString();
        }

        JLabel jlHour=new JLabel("hour:");
        JLabel jlMin=new JLabel("min:");
        JLabel jlPercent=new JLabel("percent:");
//        JLabel jlGroup=new JLabel("group:");
        final JComboBox hour=new JComboBox(h);
        final JComboBox min=new JComboBox(m);
        final JComboBox percent=new JComboBox(p);

        // 创建一个标签显示消息内容
        JLabel msg = new JLabel("Repeat:");
        final JCheckBox sun=new JCheckBox("Sun");
        final JCheckBox mon=new JCheckBox("Mon");
        final JCheckBox tue=new JCheckBox("Tue");
        final JCheckBox wed=new JCheckBox("Wed");
        final JCheckBox thu=new JCheckBox("Thu");
        final JCheckBox fri=new JCheckBox("Fri");
        final JCheckBox sat=new JCheckBox("Sat");

        // 创建一个按钮用于关闭对话框
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 关闭对话框
                dialog.dispose();
            }
        });
        JButton okBtn = new JButton("Confirm");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> week = new ArrayList<>();
                if(sun.isSelected()){
                    week.add("Sun");
                }
                if(mon.isSelected()){
                    week.add("Mon");
                }
                if(tue.isSelected()){
                    week.add("Tue");
                }
                if(wed.isSelected()){
                    week.add("Wed");
                }
                if(thu.isSelected()){
                    week.add("Thu");
                }
                if(fri.isSelected()){
                    week.add("Fri");
                }
                if(sat.isSelected()){
                    week.add("Sat");
                }
                Schedule schedule = new Schedule((int) hour.getSelectedItem(),
                        (int) min.getSelectedItem(),
                        (int) percent.getSelectedItem(),
                        week);
//                if(cbGroup.getSelectedIndex()!=-1){
//                    DeviceGroup deviceGroup = deviceGroupList.get(cbGroup.getSelectedIndex());
//                    schedule.setDeviceId(deviceGroup.getDeviceId());
//                    schedule.setGroupId(deviceGroup.getGroupId());
//                }
                scheduleList.add(schedule);
                mSchedulePresenter.insert(schedule);
                refresh();
                // 关闭对话框
                dialog.dispose();
            }
        });

        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();
        panel.setLayout(null);
        jlHour.setBounds(5,10,40,20);
        hour.setBounds(45,10,60,20);
        jlMin.setBounds(115,10,40,20);
        min.setBounds(155,10,60,20);
        jlPercent.setBounds(225,10,50,20);
        percent.setBounds(275,10,60,20);

        msg.setBounds(5,60,60,20);
        okBtn.setBounds(220,240,80,20);
        cancelBtn.setBounds(80,240,80,20);
        JPanel week = new JPanel();
        week.setLayout(new BoxLayout(week,BoxLayout.Y_AXIS));
        week.setBounds(80,60,60,170);
        week.add(sun);
        week.add(mon);
        week.add(tue);
        week.add(wed);
        week.add(thu);
        week.add(fri);
        week.add(sat);
        // 添加组件到面板
//        panel.add(messageLabel);
        panel.add(jlHour);
        panel.add(hour);
        panel.add(min);
        panel.add(jlMin);
        panel.add(jlPercent);
        panel.add(percent);

        panel.add(okBtn);
        panel.add(cancelBtn);
        panel.add(msg);
        panel.add(week);

        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);
    }

    @Override
    public void showSchedule(List<entity.Schedule> scheduleList) {
        for(entity.Schedule schedule: scheduleList){
            Schedule sd=new Schedule(schedule.getHour(),schedule.getMin(),schedule.getPercent());
            if(!schedule.getWeeks().equals("")){
                String[] scheduleArr = schedule.getWeeks().split(",");
                List<String> weeks=new ArrayList<>();
                for (int i = 0; i < scheduleArr.length; i++) {
                    String w = scheduleArr[i];
                    if(!"".equals(w)){
                        weeks.add(w);
                    }
                }
                sd.setWeek(weeks);
            }
            this.scheduleList.add(sd);
        }
        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(add)){
            showAddSchedule();
        }else if(e.getSource().equals(btnEditGroup)){
            editGroup();
        }
        else if(e.getSource().equals(jbIdentify)){
            identify();
        }else if(e.getSource().equals(delete)){
            deleteSchedule();
        }
    }

    private void deleteSchedule() {
        mSchedulePresenter.deleteAll();
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
                ScheduleView.this.updateExistedGroup(arr);
            }
        });
        groupOperation.setLocationRelativeTo(null);
        groupOperation.setVisible(true);
    }


    JCheckBox[] jcbGroup;
    List<Integer> idGroupList = new ArrayList<>();

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
//            jcbGroup[i].addActionListener(jcbGroupListener);
        }
        existedGroup.setLayout(new BoxLayout(existedGroup, BoxLayout.Y_AXIS));
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

}
