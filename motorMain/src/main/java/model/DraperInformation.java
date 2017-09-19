package model;

import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/2/13.
 */
public class DraperInformation {

    /**
     * 电机的编号 id=1,2,3,4
     */
    private SignedInteger id;

    /**
     * 方向
     */
    private Boolean reverse;

    /**
     * 当前位置
     */
    private SignedInteger curPosition;

    /**
     * 上限位
     */
    private SignedInteger upperLimit;

    /**
     * 下限位
     */
    private SignedInteger lowerLimit;

    /**
     * 预设点
     */
    private List<SignedInteger> stopList=new ArrayList<>();

    public DraperInformation(SignedInteger id, Boolean reverse, SignedInteger curPosition, SignedInteger upperLimit, SignedInteger lowerLimit, List<SignedInteger> stopList) {
        this.id=id;
        this.reverse = reverse;
        this.curPosition = curPosition;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.stopList = stopList;
    }

    public SignedInteger getId() {
        return id;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public SignedInteger getCurPosition() {
        return curPosition;
    }

    public SignedInteger getUpperLimit() {
        return upperLimit;
    }

    public SignedInteger getLowerLimit() {
        return lowerLimit;
    }

    public List<SignedInteger> getStopList() {
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
