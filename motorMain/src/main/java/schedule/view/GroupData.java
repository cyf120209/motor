package schedule.view;

public class GroupData {

    protected String groupName;

    protected boolean selected;

    public GroupData(String groupName) {
        this(groupName,false);
    }

    public GroupData(String groupName, boolean selected) {
        this.groupName = groupName;
        this.selected = selected;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        return groupName;
    }
}
