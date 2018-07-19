package schedule.view;

public class ScheduleData {

    protected String scheduleName;

    protected boolean selected;

    public ScheduleData(String scheduleName) {
        this.scheduleName = scheduleName;
        this.selected=false;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void invertSelected() {
        selected = !selected;
    }

    @Override
    public String toString() {
        return scheduleName;
    }
}
