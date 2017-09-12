package com.serotonin.bacnet4j.npdu;

import com.serotonin.bacnet4j.apdu.APDU;
import org.free.bacnet4j.util.ByteQueue;

/**
 * Created by Administrator on 2017/5/20.
 */
public class NPDU {
    public NPCI npci;
    public APDU apdu;
    public ByteQueue byteADPU;
    public NPDU(NPCI npci, APDU apdu){
        this.apdu=apdu;
        this.npci=npci;
    }
    public NPDU(NPCI npci, ByteQueue adpu){
        this.byteADPU=adpu;
        this.npci=npci;
    }
    public NPDU(ByteQueue frame){
        this.npci=new NPCI(frame);
        this.byteADPU=frame;
    }

    public void write(ByteQueue list){
        npci.write(list);
        if(apdu==null){
            list.push(byteADPU);
        }else{
            apdu.write(list);
        }
    }




}
