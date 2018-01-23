package com.serotonin.bacnet4j.npdu.uart;

public interface SpiInterruptListener {
    void received(byte[] data);
}
