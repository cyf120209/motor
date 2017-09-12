package util;

import com.serotonin.bacnet4j.type.Encodable;
import org.free.bacnet4j.util.ByteQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class DraperSubList extends Encodable {
    public List<DraperSubItem> list=new LinkedList<>();
     public  DraperSubList(ByteQueue queue){
         //4 指DraperSubItem 为4个字节
        while(queue.size()> 4){
            list.add(new DraperSubItem(queue));
        }
    }

    @Override
    public void write(ByteQueue queue) {
        for (DraperSubItem item:list){
            item.write(queue);
        }
    }

    @Override
    public void write(ByteQueue queue, int contextId) {
        for (DraperSubItem item:list){
            item.write(queue,contextId);
        }
    }

    public List<DraperSubItem> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DraperSubList that = (DraperSubList) o;

        return list != null ? list.equals(that.list) : that.list == null;

    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }
}
