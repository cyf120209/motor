package com.serotonin.bacnet4j.npdu.uart;

public interface UartSimulationPort {
    byte[] read();
    void write(byte[] data);

}
