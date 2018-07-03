package view;

import javax.swing.ImageIcon;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;

public class MemberModel{

    private static final long serialVersionUID = 1L;

    public JButton jButton = null;//显示好友头像；

    public JPanel jPanel = new JPanel();//模板容器；

    private JLabel lb_nickName = null;//显示昵称；

    private int pic;

    private String nickname = null;

    private JLabel lb_mood = null;//显示心情；


    public MemberModel(int pic, String nickname, int len) {
        super();
        this.pic = pic;//头像编（有多种方法可以实现，这种最简单）
        this.nickname = nickname;//昵称；
        initialize();
    }


    private void initialize() {
        lb_mood = new JLabel();
        lb_mood.setBounds(new Rectangle(51, 30, 131, 20));
        lb_mood.setFont(new Font("Dialog", Font.PLAIN, 12));
        lb_mood.setText("世界上最遥远的距离不是生与死,而是我站在你面前你却不知道我爱你!");
        lb_mood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                exchangeEnter();
                lb_mood.setToolTipText(lb_mood.getText());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                exchangeExited();
            }

        });
        lb_nickName = new JLabel();
        lb_nickName.setBounds(new Rectangle(52, 10, 80, 20));
        lb_nickName.setFont(new Font("Dialog", Font.BOLD, 14));
        lb_nickName.setText(nickname);
        jPanel.setSize(new Dimension(185, 60));
        jPanel.setLayout(null);
        jPanel.add(getJButton(), null);
        jPanel.add(lb_nickName, null);
        jPanel.add(lb_mood, null);
        jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent e) {
                exchangeExited();//鼠标移出模板区，改变背景颜色；
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                exchangeEnter();//鼠标移进模板区，改变背景颜色；
            }
        });
    }

    private void exchangeEnter() {
        jPanel.setBackground(new Color(192,224,248));
    }

    private void exchangeExited() {
        jPanel.setBackground(null);
    }


    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(8, 10, 40, 40));
            jButton.setBackground(new Color(236, 255, 236));
            jButton.setIcon(new ImageIcon(pic + ".jpg"));
            jButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseExited(java.awt.event.MouseEvent e) {
                    exchangeExited();//鼠标移出模板区，改变背景颜色；
                }
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    exchangeEnter();//鼠标移进模板区，改变背景颜色；
                }
            });

        }
        return jButton;
    }
}
