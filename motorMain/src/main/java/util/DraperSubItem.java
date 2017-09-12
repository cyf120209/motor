package util;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class DraperSubItem extends Encodable {
    private ObjectIdentifier devicID;
    private UnsignedInteger groupID;
    public DraperSubItem(ByteQueue queue){
        devicID=new ObjectIdentifier(queue);
        groupID=new UnsignedInteger(queue);
    }

    @Override
    public void write(ByteQueue queue) {

    }

    @Override
    public void write(ByteQueue queue, int contextId) {

    }

    public ObjectIdentifier getDevicID() {
        return devicID;
    }

    public void setDevicID(ObjectIdentifier devicID) {
        this.devicID = devicID;
    }

    public UnsignedInteger getGroupID() {
        return groupID;
    }

    public void setGroupID(UnsignedInteger groupID) {
        this.groupID = groupID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DraperSubItem item = (DraperSubItem) o;

        if (devicID != null ? !devicID.equals(item.devicID) : item.devicID != null) return false;
        return groupID != null ? groupID.equals(item.groupID) : item.groupID == null;

    }

    @Override
    public int hashCode() {
        int result = devicID != null ? devicID.hashCode() : 0;
        result = 31 * result + (groupID != null ? groupID.hashCode() : 0);
        return result;
    }
}
