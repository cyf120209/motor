import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/16.
 */
public class ShadeGroup {

    private Integer id;

    private Integer groupId;

    private Integer deviceId;

    private String groupName;

    private Set shades=new HashSet();

    public ShadeGroup() {
        super();
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

    public Set getShades() {
        return shades;
    }

    public void setShades(Set shades) {
        this.shades = shades;
    }
}
