package model;

import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/13.
 */
public class DraperInformation {

    private Boolean reverse;

    private SignedInteger curPosition;

    private SignedInteger upperLimit;

    private SignedInteger lowerLimit;

    private List<SignedInteger> stopList=new ArrayList<>();

    public DraperInformation(Boolean reverse, SignedInteger curPosition, SignedInteger upperLimit, SignedInteger lowerLimit, List<SignedInteger> stopList) {
        this.reverse = reverse;
        this.curPosition = curPosition;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.stopList = stopList;
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
}
