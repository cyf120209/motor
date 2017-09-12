package test;

/**
 * Created by lenovo on 2017/1/20.
 */
import java.awt.*;
import javax.swing.*;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Frame1 extends JFrame
{
    private JButton jButton1=new JButton();
    public Frame1 () {
        try {
            jbInit();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        this.setVisible(true);
    }

    private void jbInit () throws Exception {
        this.setBounds(300,180,400,300);
        getContentPane().setLayout(null);
        jButton1.setBounds(new Rectangle(127, 120, 139, 36));
        jButton1.setMnemonic('C');
        jButton1.setText("点我(C)");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
//                jButton1_actionPerformed(e);
                int i = JOptionPane.showConfirmDialog(null, "是否升级", "choose one", JOptionPane.YES_NO_OPTION);
                System.out.println(i);
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(jButton1);
    }
    public static void main (String[] args) {
        Frame1 frame1=new Frame1();
    }

    public void jButton1_actionPerformed (ActionEvent e) {
        this.setVisible(false);
        JFrame jf1=new JFrame("子窗口");
        jf1.setBounds(100,50,800,600);
        jf1.setDefaultCloseOperation(jf1.EXIT_ON_CLOSE);
        jf1.setVisible(true);

    }
}
