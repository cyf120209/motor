package entity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private int id;

    private int hour;

    private int min;

    private int percent;

    private String weeks;

    private String scheduleName;

    /**
     * 默认值为 1
     * 1 启用
     * 0 关闭
     */
    private int isEnabled;

    private List<ShadeGroup> shadeGroups =new ArrayList<ShadeGroup>();

    public Schedule() {
    }

    public Schedule(int hour, int min, int percent,String scheduleName) {
        this(hour,min,percent,"",scheduleName);
    }

    public Schedule(int hour, int min, int percent,String weeks,String scheduleName) {
        this.hour = hour;
        this.min = min;
        this.percent = percent;
        this.scheduleName=scheduleName;
        this.weeks = weeks;
        this.isEnabled=1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<ShadeGroup> getShadeGroups() {
        return shadeGroups;
    }

    public void setShadeGroups(List<ShadeGroup> shadeGroups) {
        this.shadeGroups = shadeGroups;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
}
