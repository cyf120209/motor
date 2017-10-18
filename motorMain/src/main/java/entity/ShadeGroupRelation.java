package entity;

/**
 * Created by lenovo on 2017/6/16.
 */
public class ShadeGroupRelation {

    private Integer id;

    private Integer shadeGroupId;

    private Integer shadeId;

    public ShadeGroupRelation() {
        super();
    }

    public ShadeGroupRelation(Integer shadeGroupId, Integer shadeId) {
        this.shadeGroupId = shadeGroupId;
        this.shadeId = shadeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShadeGroupId() {
        return shadeGroupId;
    }

    public void setShadeGroupId(Integer shadeGroupId) {
        this.shadeGroupId = shadeGroupId;
    }

    public Integer getShadeId() {
        return shadeId;
    }

    public void setShadeId(Integer shadeId) {
        this.shadeId = shadeId;
    }
}
