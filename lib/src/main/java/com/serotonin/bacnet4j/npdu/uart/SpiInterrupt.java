package com.serotonin.bacnet4j.npdu.uart;

import java.util.Vector;

public class SpiInterrupt {

    private static Vector<SpiInterruptListener> listeners = new Vector<>();

    public static synchronized void addListener(SpiInterruptListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    public static void receivedData(byte[] data) {
        Vector<SpiInterruptListener> listenersClone;
        listenersClone = (Vector<SpiInterruptListener>) listeners.clone();

        for (int i = 0; i < listenersClone.size(); i++) {
            SpiInterruptListener listener = listenersClone.elementAt(i);
            if(listener != null) {
                listener.received(data);
            }
        }
    }

    public static Vector<SpiInterruptListener> getListeners() {
        return listeners;
    }
}
