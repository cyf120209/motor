package util;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import org.free.bacnet4j.util.ByteQueue;

/**
 * Created by lenovo on 2016/12/17.
 */
public class DraperGroupSubList extends Encodable {
    public static  class  DraperGroupSubscription extends Primitive {
        ObjectIdentifier deviceIdentifier;
        GroupIdentifier groupIdentifier;
        @Override
        protected void writeImpl(ByteQueue queue) {
            deviceIdentifier.writeImpl(queue);
            groupIdentifier.writeImpl(queue);
        }

        @Override
        protected long getLength() {
            return 0;
        }

        @Override
        protected byte getTypeId() {
            return 0;
        }
    }
    @Override
    public void write(ByteQueue queue) {

    }

    @Override
    public void write(ByteQueue queue, int contextId) {

    }
}
