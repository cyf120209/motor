package entity;

public class ScheduleGroupRelation {

    private Integer id;

    private Integer scheduleGroupId;

    private Integer scheduleId;

    public ScheduleGroupRelation() {
    }

    public ScheduleGroupRelation(Integer scheduleGroupId, Integer scheduleId) {
        this.scheduleGroupId = scheduleGroupId;
        this.scheduleId = scheduleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScheduleGroupId() {
        return scheduleGroupId;
    }

    public void setScheduleGroupId(Integer scheduleGroupId) {
        this.scheduleGroupId = scheduleGroupId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }
}
