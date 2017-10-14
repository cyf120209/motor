package main.view;

import util.ComPortutils;
import main.view.BoxLayoutCase;
import util.MyBatisUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/18.
 */
public class Main{

    private BoxLayoutCase mBoxLayoutCase;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.guiInit();
        //初始化数据库
        MyBatisUtils.init();
        //周期性更新数据库
        main.updateDatabase();
    }

    private void updateDatabase() {
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        timer.start();
    }

    private void guiInit() throws ParseException {
        mBoxLayoutCase = new BoxLayoutCase();
    }
}