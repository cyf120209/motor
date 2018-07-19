package manager.impl;

import dao.ScheduleDao;
import entity.Schedule;
import util.Draper;
import util.Public;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;

public class ScheduleManger {

    public static void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ScheduleManger().start();
            }
        }).run();
    }

    private void start(){
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScheduleDao scheduleDao = new ScheduleDao();
                List<Schedule> scheduleList = scheduleDao.queryAll();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);
                for(Schedule schedule:scheduleList){
                    if(schedule.getHour()==hour && schedule.getMin()==min && sec==0){
//                        List<String> week = schedule.getWeeks();
//                        if(week==null) continue;
//                        for (String w:week) {
//                            int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                            if (todayOfWeek == Public.week2Int(w)){
//                                try {
//                                    System.out.println("sendCmd");
//                                    Draper.sendCmd(schedule.getDeviceId(),schedule.getGroupId(),schedule.getPercent());
//                                } catch (Exception e1) {
//                                    e1.printStackTrace();
//                                }
//                            }
//                        }
                    }
                }
            }
        }).start();

    }

}
