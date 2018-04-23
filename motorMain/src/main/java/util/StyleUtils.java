package util;

import limitsAndStops.view.LimitsAndStops;

import javax.swing.*;
import java.net.URL;

/**
 * Created by lenovo on 2017/10/13.
 */
public class StyleUtils {

    private static ImageIcon formBg;
    private static ImageIcon btnBg;
    static {
        URL btnURL = LimitsAndStops.class.getResource("/img/btn.png");
        URL url = LimitsAndStops.class.getResource("/img/bg.jpg");
        formBg = new ImageIcon(url);
        btnBg = new ImageIcon(btnURL);
    }

    public static ImageIcon getFormBg(){
        return formBg;
    }

    public static ImageIcon getBtnBg(){
        return btnBg;
    }

    public static void setBtnBg(JButton jButton){
        jButton.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
        jButton.setContentAreaFilled(false);//设置图片填满按钮所在的区域
    }

    public static void setObjextBg(AbstractButton button){
//        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
        button.setContentAreaFilled(false);//设置图片填满按钮所在的区域
    }

    public static void setObject(JComponent button){
//        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
    }
}
