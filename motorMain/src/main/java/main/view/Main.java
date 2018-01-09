package main.view;

import manager.impl.MyManager;

import java.text.ParseException;

/**
 * Created by lenovo on 2017/1/18.
 */
public class Main{

    private BoxLayoutCase mBoxLayoutCase;


    public static void main(String[] args) throws Exception {
        final Main main = new Main();
        main.guiInit();
        MyManager manager = new MyManager();
        manager.init();
    }



    private void guiInit() throws ParseException {
        mBoxLayoutCase = new BoxLayoutCase();
    }
}