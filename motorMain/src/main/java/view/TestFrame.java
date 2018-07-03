package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class TestFrame {

    private JFrame jFrame = null;

    private JPanel jContentPane = null;

    private JScrollPane scrollPane=null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TestFrame application = new TestFrame();
                application.getJFrame().setVisible(true);
            }
        });
    }

    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setSize(230, 700);
            jFrame.setTitle("模拟实现QQ面板功能");
            jFrame.setContentPane(getJContentPane());
        }
        return jFrame;
    }


    private JScrollPane getScrollPane(){//给添加好友的容器JPanel添加滚动条；
        if(scrollPane==null){
            scrollPane=new JScrollPane(new TestPane());
            scrollPane.setBounds(20,5, -1, 600);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );//不显示水平滚动条；
        }
        return scrollPane;
    }


    private JPanel getJContentPane() {//实例化底层的容器JPanel；
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }
}
