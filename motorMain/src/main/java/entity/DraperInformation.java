package entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/13.
 */
public class DraperInformation implements Serializable{

    /**
     * 电机的编号 id=1,2,3,4
     */
    private Integer id;

    /**
     * 方向
     */
    private Boolean reverse;

    /**
     * 当前位置
     */
    private Integer curPosition;

    /**
     * 上限位
     */
    private Integer upperLimit;

    /**
     * 下限位
     */
    private Integer lowerLimit;

    /**
     * 预设点
     */
    private List<Integer> stopList=new ArrayList<>();

    public DraperInformation(int id, Boolean reverse, int curPosition, int upperLimit, int lowerLimit, List<Integer> stopList) {
        this.id=id;
        this.reverse = reverse;
        this.curPosition = curPosition;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.stopList = stopList;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public Integer getCurPosition() {
        return curPosition;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public Integer getLowerLimit() {
        return lowerLimit;
    }

    public List<Integer> getStopList() {
        return stopList;
    }

    @Override
    public String toString() {
        return "reverse：" + reverse +
                ", position：" + curPosition +
                ", upperLimit：" + upperLimit +
                ", lowerLimit：" + lowerLimit +
                '}'+"\r\n";
    }
}
