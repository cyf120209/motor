package com.serotonin.bacnet4j.npdu.mstp;

public interface RemoteDeviceListener {

    void remoteDeviceAdd(byte addr);

    void remoteDeviceDelete(byte addr);
}
