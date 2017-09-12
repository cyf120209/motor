package util;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

import java.math.BigInteger;

/**
 * Created by lenovo on 2016/12/17.
 */
public class GroupIdentifier extends UnsignedInteger {
    public GroupIdentifier(int value) {
        super(value);
    }

    public GroupIdentifier(long value) {
        super(value);
    }

    public GroupIdentifier(BigInteger value) {
        super(value);
    }

    public GroupIdentifier(ByteQueue queue) {
        super(queue);
    }

    @Override
    public void writeImpl(ByteQueue queue) {
        super.writeImpl(queue);
    }

    @Override
    public void writeContextTag(ByteQueue queue, int contextId, boolean start) {
        super.writeContextTag(queue, contextId, start);
    }
}
