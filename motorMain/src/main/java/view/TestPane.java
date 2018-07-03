package view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class TestPane extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel11 = null;
    private JLabel jLabel12 = null;
    private int clickF=0;
    private int clickB=0;

    public TestPane() {
        super();
        initialize();
    }

    private void initialize() {

        jLabel12 = new JLabel();
        jLabel12.setIcon(new ImageIcon("img/bg1.jpg"));
        jLabel12.add(new MemberModel(3,"CoolBabY3",150).jPanel);
        jLabel12.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        jLabel11 = new JLabel();
        jLabel11.setIcon(new ImageIcon("img/bg1.jpg"));
        jLabel11.add(new MemberModel(2,"CoolBabY2",200).jPanel);
        jLabel11.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        jLabel1 = new JLabel();
        jLabel1.setIcon(new ImageIcon("img/bg1.jpg"));
        jLabel1.add(new MemberModel(1,"CoolBabY1",200).jPanel);
        jLabel1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        jLabel = new JLabel();
        jLabel.setText("我的好友");
        jLabel.setIcon(new ImageIcon("img/ico.jpg"));
        jLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clickF+=1;
                if(clickF%2==1){
                    jLabel1.setVisible(false);
                    jLabel11.setVisible(false);
                    jLabel12.setVisible(false);
                    jLabel.setIcon(new ImageIcon("img/ico2.jpg"));
                    update();
                }else{
                    jLabel1.setVisible(true);
                    jLabel11.setVisible(true);
                    jLabel12.setVisible(true);
                    jLabel.setIcon(new ImageIcon("img/ico.jpg"));
                    update();
                }
            }
        });
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(200, 408);
        this.setLocation(20, 5);
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(jLabel11, null);
        this.add(jLabel12, null);
        addJLabel();
    }

    private void update(){//更新UI界面；
        this.updateUI();
    }

    private void clickBlack2(JLabel []jb){//点击标签，将后面的标签全部设为不可视；
        for(int i=1;i<jb.length;i++){
            try{
                jb[i].setVisible(false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        update();
    }
    private void clickBlack(JLabel []jb){//点击标签，将后面的标签全部设为可视；
        for(int i=1;i<jb.length;i++){
            try{
                jb[i].setVisible(true);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        update();
    }
    private void addJLabel(){//添加黑名单的内容；
        final JLabel []jb=new JLabel[7];
        jb[0] = new JLabel();
        jb[0].setText("黑名单");
        jb[0].setIcon(new ImageIcon("img/ico2.jpg"));
        jb[0].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jb[0].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {

                clickB+=1;
                if(clickB%2==1){
                    clickBlack(jb);
                    jb[0].setIcon(new ImageIcon("img/ico.jpg"));
                }else{
                    clickBlack2(jb);
                    jb[0].setIcon(new ImageIcon("img/ico2.jpg"));
                }

            }
        });
        this.add(jb[0],null);
        for(int i=1;i<jb.length;i++){
            jb[i]=new JLabel();
            jb[i].setIcon(new ImageIcon("img/bg.jpg"));
            jb[i].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            jb[i].add(new MemberModel((i+3),"CoolBabY"+(i+3),200).jPanel);
            jb[i].setVisible(false);
            this.add(jb[i],null);
        }

    }
}