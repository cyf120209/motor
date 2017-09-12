package com.serotonin.bacnet4j.util;

import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;

/**
 * Created by lenovo on 2016/12/14.
 */
public class ByteBuilder {
    byte[] tmpBuffer=new byte[600];
    int index;
    public ByteBuilder(){
        index=0;
    }
    public ByteBuilder append(byte d){
        tmpBuffer[index]=d;
        index++;
        return  this;
    }

    public byte[] toArrays(){
        byte[] re=new byte[index];
        System.arraycopy(tmpBuffer,0,re,0,index);
        return re;
    }
}
