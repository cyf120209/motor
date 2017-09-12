package main.view;

import com.serotonin.bacnet4j.RemoteDevice;

/**
 * Created by lenovo on 2017/1/18.
 */
public interface BoxLayoutView {

    void updatedevBox(String text,boolean isRemove);

    void updateVersionLabel(String text);

    void AddItem(RemoteDevice d);

    String getCmdTextEdit();

    RemoteDevice getdevBoxSelectedItem();

    String getComBoxSelectedItem();



}
