package util;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import org.free.bacnet4j.util.ByteQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class DraperInformationItem extends Encodable{

    private Boolean isReverse;
    private SignedInteger curPos;
    private SignedInteger upLimit;
    private SignedInteger lowLimit;
    private LinkedList<SignedInteger> persetStop=new LinkedList<>() ;
    public DraperInformationItem(ByteQueue queue) {
        isReverse=new Boolean(queue);
        curPos=new SignedInteger(queue);
        upLimit=new SignedInteger(queue);
        lowLimit=new SignedInteger(queue);
        //2 指SignedInteger 占两个字节
        while (queue.size()> 2){
            persetStop.add(new SignedInteger(queue));
        }
    }

    public Boolean getIsReverse() {
        return isReverse;
    }

    public SignedInteger getCurPos() {
        return curPos;
    }

    public SignedInteger getUpLimit() {
        return upLimit;
    }

    public SignedInteger getLowLimit() {
        return lowLimit;
    }

    public LinkedList<SignedInteger> getPersetStop() {
        return persetStop;
    }

    @Override
    public void write(ByteQueue queue) {

    }

    @Override
    public void write(ByteQueue queue, int contextId) {

    }
}
