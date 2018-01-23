package com.serotonin.bacnet4j.npdu.uart;

import java.util.Arrays;

/**
 * Created by lenovo on 2016/12/14.
 */
public class ByteBuilder {

    byte[] value;
    int count = 0;

    public ByteBuilder() {
        value = new byte[2048];
    }

    public ByteBuilder(int capacity) {
       value = new byte[capacity];
    }

    public ByteBuilder append(byte d) {
        ensureCapacityInternal(count+1);
        value[count] = d;
        count++;
        return this;
    }

    public ByteBuilder append(byte[] bytes) {
        int length = bytes.length;
        ensureCapacityInternal(count+length);
        System.arraycopy(bytes, 0, value, count, length);
        count = count + length;
        return this;
    }

    public ByteBuilder append(byte[] bytes, int srcOffset) {
        int length=bytes.length-srcOffset;
        ensureCapacityInternal(count+length);
        System.arraycopy(bytes, srcOffset, value, count, length);
        count = count + length;
        return this;
    }

    public byte[] toArrays() {
        byte[] re = new byte[count];
        System.arraycopy(value, 0, re, 0, count);
        return re;
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        if(minimumCapacity-value.length>0){
            expandCapacity(minimumCapacity);
        }
    }

    private void expandCapacity(int minimumCapacity) {
        int newCapacity=value.length*2;
        if(newCapacity<minimumCapacity){
            newCapacity=minimumCapacity;
        }
        if(newCapacity<0){
            if (minimumCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }
}
