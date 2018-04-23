package entity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private int hour;

    private int min;

    private int percent;

    private List<String> week=new ArrayList<>();

    private JPanel jPanel;

    public Schedule(int hour, int min, int percent) {
        this(hour,min,percent,new ArrayList<String>());
    }

    public Schedule(int hour, int min, int percent, List<String> week) {
        this.hour = hour;
        this.min = min;
        this.percent = percent;
        this.week = week;
        initialize();
    }

    private void initialize(){
        jPanel=new JPanel();
        jPanel.setOpaque(false);
        jPanel.setBounds(0,0,100,50);
        JLabel time=new JLabel(hour+":"+min);
        time.setBounds(5,5,80,20);
        JLabel per=new JLabel(""+percent);
        per.setBounds(90,5,50,20);
        JLabel weeks=new JLabel(getWeekString());
        weeks.setBounds(5,5,80,20);
        jPanel.add(time);
        jPanel.add(per);
        jPanel.add(weeks);
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public List<String> getWeek() {
        return week;
    }

    public void setWeek(List<String> week) {
        this.week = week;
    }

    public String getWeekString(){
        String weekStr="";
        for (String str:week){
            weekStr+=str+" ";
        }
        return weekStr.trim();
    }
}
