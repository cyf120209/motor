package util;

import com.serotonin.bacnet4j.type.Encodable;
import org.free.bacnet4j.util.ByteQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/11.
 */
public class DraperInformationList extends Encodable {

    public List<DraperInformationItem> list=new LinkedList<>();

    public DraperInformationList(ByteQueue queue) {
        while (queue.size()>4){
            list.add(new DraperInformationItem(queue));
        }
    }

    @Override
    public void write(ByteQueue queue) {

    }

    @Override
    public void write(ByteQueue queue, int contextId) {

    }
}
