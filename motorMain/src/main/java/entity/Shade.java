package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/6/16.
 */
public class Shade implements Serializable {

    private Integer id;

    private Integer shadeId;

    private String shadeName;

    private Integer shadePosition;

    private Integer shadePriority;

    private String shadeStatus;

//    private Set shadeGroups=new HashSet();
    private List<ShadeGroup> shadeGroups=new ArrayList<ShadeGroup>();


    public Shade() {
        super();
    }

    public Shade(Integer shadeId, String shadeName, Integer shadePosition, Integer shadePriority, String shadeStatus ) {
        this.shadeId = shadeId;
        this.shadeName = shadeName;
        this.shadePosition = shadePosition;
        this.shadePriority = shadePriority;
        this.shadeStatus = shadeStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShadeId() {
        return shadeId;
    }

    public void setShadeId(Integer shadeId) {
        this.shadeId = shadeId;
    }

    public String getShadeName() {
        return shadeName;
    }

    public void setShadeName(String shadeName) {
        this.shadeName = shadeName;
    }

    public Integer getShadePosition() {
        return shadePosition;
    }

    public void setShadePosition(Integer shadePosition) {
        this.shadePosition = shadePosition;
    }

    public Integer getShadePriority() {
        return shadePriority;
    }

    public void setShadePriority(Integer shadePriority) {
        this.shadePriority = shadePriority;
    }

    public String getShadeStatus() {
        return shadeStatus;
    }

    public void setShadeStatus(String shadeStatus) {
        this.shadeStatus = shadeStatus;
    }

    public List<ShadeGroup> getShadeGroups() {
        return shadeGroups;
    }

    public void setShadeGroups(List<ShadeGroup> shadeGroups) {
        this.shadeGroups = shadeGroups;
    }

    //    public Set getShadeGroups() {
//        return shadeGroups;
//    }
//
//    public void setShadeGroups(Set shadeGroups) {
//        this.shadeGroups = shadeGroups;
//    }
}
