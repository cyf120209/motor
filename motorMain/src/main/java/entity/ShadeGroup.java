package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/16.
 */
public class ShadeGroup implements Serializable{

    private Integer id;

    private Integer groupId;

    private Integer deviceId;

    private String groupName;

    private List<Shade> shades =new ArrayList<Shade>();

    public ShadeGroup() {
        super();
    }

    public ShadeGroup(Integer groupId, Integer deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Shade> getShades() {
        return shades;
    }

    public void setShades(List<Shade> shades) {
        this.shades = shades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;

        ShadeGroup that = (ShadeGroup) o;

        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        return groupName != null ? groupName.equals(that.groupName) : that.groupName == null;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}
