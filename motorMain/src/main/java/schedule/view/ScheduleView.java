package schedule.view;

import common.Common;
import entity.Schedule;
import util.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ScheduleView extends JFrame implements ActionListener{

    JButton add=new JButton("+");
    private List<Schedule> scheduleList=new ArrayList<>();

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

        add.setBounds(300,5,50,30);
        StyleUtils.setBtnBg(add);
        add(add);

        JLabel sd = new JLabel();
        sd.setBounds(0,0,200,100);
        sd.add(new Schedule(6,10,60).getjPanel());
        add(sd);
        add.addActionListener(this);

    }

    private void addSchedulePanel(Schedule schedule){
        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setBounds(0,0,100,50);
        JLabel time=new JLabel(schedule.getHour()+":"+schedule.getMin());
        JLabel percent=new JLabel(""+schedule.getPercent());
        JLabel week=new JLabel(schedule.getWeekString());
        time.setBounds(5,5,100,20);
        percent.setBounds(115,5,60,20);
        week.setBounds(5,30,150,20);
        panel.add(time);
        panel.add(percent);
        panel.add(week);
    }

    private void refresh(){
        invalidate();
    }

    private void showAddSchedule(){
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(this, "Add Schedule", true);
        // 设置对话框的宽高
        dialog.setSize(250, 275);
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
                scheduleList.add(schedule);
                refresh();
                // 关闭对话框
                dialog.dispose();
            }
        });

        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();
        panel.setLayout(null);
        hour.setBounds(10,10,60,20);
        min.setBounds(90,10,60,20);
        percent.setBounds(170,10,60,20);
        msg.setBounds(10,40,60,20);
        okBtn.setBounds(150,200,80,20);
        cancelBtn.setBounds(20,200,80,20);
        JPanel week = new JPanel();
        week.setLayout(new BoxLayout(week,BoxLayout.Y_AXIS));
        week.setBounds(80,40,60,150);
        week.add(sun);
        week.add(mon);
        week.add(tue);
        week.add(wed);
        week.add(thu);
        week.add(fri);
        week.add(sat);
        // 添加组件到面板
//        panel.add(messageLabel);
        panel.add(hour);
        panel.add(min);
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
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(add)){
            showAddSchedule();
        }
    }

}
