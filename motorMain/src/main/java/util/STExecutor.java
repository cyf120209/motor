package util;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.Unsigned8;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2016/12/17.
 */
public class STExecutor {
    public static final int DRAPER_CMD_STOP=1;
    public static final int DRAPER_CMD_RETRACTED=3;
    public static final int DRAPER_CMD_EXTENDED=4;
    public static final int DRAPER_VENDOR_ID=900;
    public static final int CMD_UNCONF_SERNUM=1;
    public static final int HAVEFRAME_UNCONF_SERNUM=2;
    public static final int GETFRAME_CONF_SERSUM=3;
    public static final int FRAME_BLOCK_UNCONF_SEERNUM=4;
    public static final int SUBSCRIPTION_GROUP=5;
    public static final int ANNOUNCE_GROUP=6;

    public static final String GET_FRIMEBLOCK_PRODUCT_MODE_NAME="Product Model Name";
    public static final String GET_FRIMEBLOCK_START_OFFSET="Start Offset";
    public static final String GET_FRIMEBLOCK_BLOCK_SZIE="Block Size";

    public static final UnsignedInteger cmdSer=new UnsignedInteger(CMD_UNCONF_SERNUM);
    private static final String TAG = STExecutor.class.getSimpleName();
    public static UnsignedInteger vendorID=new UnsignedInteger(DRAPER_VENDOR_ID);
    public static UnsignedInteger haveFrame=new UnsignedInteger(HAVEFRAME_UNCONF_SERNUM);
    public static UnsignedInteger brocastFram=new UnsignedInteger(FRAME_BLOCK_UNCONF_SEERNUM);
    public static UnsignedInteger subscription=new UnsignedInteger(SUBSCRIPTION_GROUP);
    public static UnsignedInteger announce=new UnsignedInteger(ANNOUNCE_GROUP);
    public static ObjectIdentifier deviceid=new ObjectIdentifier(ObjectType.device,900900);


    static ExecutorService singleThreadExecutor=Executors.newSingleThreadExecutor();

    public static void submit(Runnable runnable){
        singleThreadExecutor.submit(runnable);
    }

    public static boolean getState(Runnable runnable){
        return singleThreadExecutor.isShutdown();
    }

    public static void shutdown(Runnable runnable){
        singleThreadExecutor.shutdownNow();
    }

    public static void announce(final LocalDevice device, final SequenceOf<Primitive> listParam){
        singleThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    device.sendGlobalBroadcast(new UnconfirmedPrivateTransferRequest(vendorID,announce,listParam));
                } catch (BACnetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
