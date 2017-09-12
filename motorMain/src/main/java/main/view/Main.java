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

    private static BoxLayoutCase mBoxLayoutCase;
    private static List<String> mListPort=new LinkedList<>();

    public static void main(String[] args) throws Exception {
        guiInit();
        MyBatisUtils.init();
    }

    private static void guiInit() throws ParseException {
        mBoxLayoutCase = new BoxLayoutCase();
    }
}