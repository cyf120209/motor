package manager.impl;

import entity.Schedule;

import java.util.Calendar;

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
        //TODO 获取 schedules
        while (true){
            Schedule schedule = new Schedule();
            Calendar calendar = Calendar.getInstance();
            int _hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(schedule.getHour()==_hour){
                int _min = calendar.get(Calendar.MINUTE);
                if(schedule.getMin()==_min){

                }
            }

        }

    }

}
