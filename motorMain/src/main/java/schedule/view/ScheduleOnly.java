package schedule.view;

import common.Common;
import groupOperation.IGroupCallback;
import groupOperation.view.GroupOperation;
import model.DeviceGroup;
import schedule.presenter.ISchedulePresenter;
import schedule.presenter.SchedulePresenterImpl;
import util.Draper;
import util.MyLocalDevice;
import util.StyleUtils;
import view.Schedule;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ScheduleOnly extends JFrame implements IScheduleOnly,ActionListener{

    private final ISchedulePresenter mSchedulePresenter;
    JButton add=new JButton("add");
    JButton delete=new JButton("delete");
    JButton editSchedule=new JButton("edit");
    JButton relation=new JButton("Relation");
    private List<Schedule> scheduleList=new ArrayList<>();

    private JTable table = null;
    private JScrollPane sPane = null;
    private TableModel mTableModel;

    private List<DeviceGroup> deviceGroupList=new ArrayList<>();

    private List<JCheckBox> jbScheduleList=new ArrayList<>();

    /**
     * 选中schedule的行号
     */
    private int scheduleIndex=-1;

    /**
     * scheduleName 专用
     */
    private long scheduleMaxId;

    public ScheduleOnly() throws HeadlessException {
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

//        jpScheduleContainer.setBounds(0,0,200,400);
        add.setBounds(230,400,100,30);
        delete.setBounds(10,400,100,30);
        editSchedule.setBounds(120,400,100,30);
        relation.setBounds(340,400,100,30);

        StyleUtils.setBtnBg(add);
        add(add);
        add(delete);
        add(editSchedule);
        add(relation);

        add.addActionListener(this);
        delete.addActionListener(this);
        editSchedule.addActionListener(this);
        relation.addActionListener(this);

        //初始化表格
        mTableModel = new TableModel();
        table = new JTable(mTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置字体
        Font font = new Font("Menu.font", Font.PLAIN, 13);
        table.setFont(font);
        JTableHeader header = table.getTableHeader();
        header.setFont(font);
        header.setPreferredSize(new Dimension(header.getWidth(), 60));
        table.setRowHeight(40);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        // tcr.setHorizontalAlignment(JLabel.CENTER);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
            /*将每一列的默认宽度设置为100*/
        table.getColumnModel().getColumn(0).setPreferredWidth(10);

        sPane = new JScrollPane(table);

        sPane.setBounds(20, 30, getWidth() - 50, getHeight() - 200);
        add(sPane);
        table.updateUI();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int r = table.getSelectedRow();
                int c = table.getSelectedColumn();
                Boolean value = (Boolean) mTableModel.getValueAt(r, 0);

                if(c==0) {
                    if (scheduleIndex != -1) {
                        mTableModel.setValueAt(false, scheduleIndex, 0);
                    }
                    mTableModel.setValueAt(true, r, 0);
                    scheduleIndex = r;
                }else {
                    if(value){
                        return;
                    }
                    if (scheduleIndex != -1) {
                        mTableModel.setValueAt(false, scheduleIndex, 0);
                    }
                    mTableModel.setValueAt(true, r, 0);
                    scheduleIndex = r;
                }
//                List<String> selected = mTableModel.getUpgradeSelected();
//                if (selected.size() == 0) {
//                }
            }
        });

        mSchedulePresenter = new SchedulePresenterImpl(this);
    }

    private void refresh(){
        table.updateUI();
    }

    private void showAddSchedule(){
        showAddSchedule(null);
    }

    private void showAddSchedule(final Schedule sd){
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(this, "Add Schedule", true);
        // 设置对话框的宽高
        dialog.setSize(370, 295);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(this);

        Integer[] h=new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
        Integer[] m=new Integer[60];
        for(int i=0;i<60;i++){
            m[i]=i;
        }
        Integer[] p=new Integer[101];
        for(int i=0;i<=100;i++){
            p[i]=i;
        }

        final JTextField jfName=new JTextField();
        JLabel jlHour=new JLabel("hour:");
        JLabel jlMin=new JLabel("min:");
        JLabel jlPercent=new JLabel("percent:");
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

        //初始化更新数据
        if(sd!=null) {
            hour.setSelectedItem(sd.getHour());
            min.setSelectedItem(sd.getMin());
            percent.setSelectedItem(sd.getPercent());
            jfName.setText(sd.getScheduleName());
            List<String> scheduleWeek = sd.getWeek();
            for (String week : scheduleWeek) {
                switch (week) {
                    case "Sun":
                        sun.setSelected(true);
                        break;
                    case "Mon":
                        mon.setSelected(true);
                        break;
                    case "Tue":
                        tue.setSelected(true);
                        break;
                    case "Wed":
                        wed.setSelected(true);
                        break;
                    case "Thu":
                        thu.setSelected(true);
                        break;
                    case "Fri":
                        fri.setSelected(true);
                        break;
                    case "Sat":
                        sat.setSelected(true);
                        break;
                }
            }
        }else {
            // TODO scheduleName
            jfName.setText("schedule"+(scheduleMaxId+1));
        }

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
                        jfName.getText().toString().trim(),
                        week);
                if(sd!=null){
                    int index = scheduleList.indexOf(sd);
                    scheduleList.set(index,schedule);
                    int update = mSchedulePresenter.update(scheduleIndex, schedule);
                    if(update>0){
                        mTableModel.updateRow(scheduleIndex,schedule);
                    }
                }else {
                    scheduleList.add(schedule);
                    long insert = mSchedulePresenter.insert(schedule);
                    if(insert >0){
                        mTableModel.addRow(schedule);
                        scheduleMaxId++;
                    }else {
                        System.out.println("failed");
                    }
                }
                refresh();
                // 关闭对话框
                dialog.dispose();
            }
        });

        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();
        panel.setLayout(null);
        jfName.setBounds(5,10,100,20);
        jlHour.setBounds(5,30,40,20);
        hour.setBounds(45,30,60,20);
        jlMin.setBounds(115,30,40,20);
        min.setBounds(155,30,60,20);
        jlPercent.setBounds(225,30,50,20);
        percent.setBounds(275,30,60,20);

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
        panel.add(jfName);
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
        if(scheduleList==null || scheduleList.size()==0){
            scheduleMaxId=0;
        }else {
            scheduleMaxId=scheduleList.get(scheduleList.size()-1).getId();
        }
        for(entity.Schedule schedule: scheduleList){
            String scheduleName = (schedule.getScheduleName()==null)?"":schedule.getScheduleName();
            Schedule sd=new Schedule(schedule.getHour(),schedule.getMin(),schedule.getPercent(), scheduleName);
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
            mTableModel.addRow(sd);
        }
        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(add)){
            showAddSchedule();
        }else if(e.getSource().equals(delete)){
            deleteSchedule();
        }else if(e.getSource().equals(editSchedule)){
            editSchedule();
        }else if(e.getSource().equals(relation)){
            editRelation();
        }
    }

    private void editRelation() {
        ScheduleRelation scheduleRelation = new ScheduleRelation();
        scheduleRelation.setLocationRelativeTo(null);
        scheduleRelation.setVisible(true);
    }

    private void deleteSchedule() {
        mSchedulePresenter.delete(scheduleIndex);
        mTableModel.removeRow(scheduleIndex);
        refresh();
    }

    private void editSchedule(){
        showAddSchedule(scheduleList.get(scheduleIndex));
    }


}
