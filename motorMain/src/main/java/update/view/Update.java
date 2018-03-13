package update.view;

import com.serotonin.bacnet4j.RemoteDevice;
import common.Common;
import update.presenter.FirmWareInformation;
import update.presenter.UpdatePresenter;
import update.presenter.UpdatePresenterImpl;
import util.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/1/20.
 */
public class Update extends JFrame implements UpdateView, ActionListener {

    private final UpdatePresenter mUpdatePresenter;
//    public JButton choosebt = new JButton("choose file");
    public JButton choosebt = new JButton("c1");
    public JButton choosebt2 = new JButton("c2");
    public JButton updateToSelectButton = new JButton("Upgrade select");
    public JButton updateButton = new JButton("Upgrade All");
    public JTextField jfDelay = new JTextField();

    public JLabel loclFile = new JLabel("");
    public JLabel loclFile2 = new JLabel("");
    public JLabel type = new JLabel("Type:");
    public JLabel typehText = new JLabel("type Num:");
    public JLabel lastModifiy = new JLabel("lastModifiy:");
    public JLabel majorText = new JLabel("Major Num:");
    public JLabel minorText = new JLabel("Minor Num:");
    public JLabel patchText = new JLabel("Patch Num:");
    public JLabel typeText = new JLabel("");
    public JLabel typeLabel = new JLabel("");
    public JLabel lastModifiyText = new JLabel("");
    public JLabel majorLabel = new JLabel("");
    public JLabel minorLabel = new JLabel("");
    public JLabel patchLabel = new JLabel("");
    public JComboBox devBox = new JComboBox();

    public JLabel versionLabel = new JLabel("Versin: No Version");
    public JButton ReadVersion = new JButton("readVersion");
    public JButton ReadValue = new JButton("readValue");

    private JProgressBar pb = new JProgressBar();

    private JCheckBox jcSinge = new JCheckBox("single");
    private JButton jbAuto = new JButton("自动循环");
    private JLabel jlAutoCount = new JLabel("0");

    public JList draperOriginal = new JList();
    JScrollPane draperOriginalJSP = new JScrollPane(draperOriginal);
    public JList draperBeforeUpgrade = new JList();
    JScrollPane draperBeforeUpgradeJSP = new JScrollPane(draperBeforeUpgrade);
    public JList draperAfterUpgrade = new JList();
    JScrollPane draperAfterUpgradeJSP = new JScrollPane(draperAfterUpgrade);

    public JList upgradeInformation = new JList();
    JScrollPane upgradeInformationJSP = new JScrollPane(upgradeInformation);

    private JButton exit=new JButton("退出");

    // 121或212 测试
    private JButton jOne2Two=new JButton("update 121");

    public Update() throws HeadlessException {
        mUpdatePresenter = new UpdatePresenterImpl(this);
        setTitle("update v1.1");
        setLayout(null);
        setSize(Common.SCREEN_WEIGHT,Common.SCREEN_HEIGHT+50);
//        setSize(700,400);
        //不要边框 需放置在组件添加之前，否则不生效
//        setUndecorated(true);

        // 把背景图片显示在一个标签里面
        JLabel label = new JLabel(StyleUtils.getFormBg());
        // 把标签的大小位置设置为图片刚好填充整个面板
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        loclFile.setBounds(70, 5, 500, 20);
//        loclFile.setBounds(120, 5, 500, 20);
        loclFile2.setBounds(70, 25, 500, 20);

        choosebt.setBounds(new Rectangle(10, 5, 50, 20));
//        choosebt.setBounds(new Rectangle(10, 5, 100, 20));
        choosebt2.setBounds(new Rectangle(10, 25, 50, 20));
        updateToSelectButton.setBounds(340, 50, 130, 20);
        updateButton.setBounds(470, 50, 130, 20);
        jfDelay.setBounds(600, 50, 80, 20);

        devBox.setBounds(10, 150, 300, 20);

        type.setBounds(10, 50, 70, 20);
        typeText.setBounds(80, 50, 100, 20);

        typehText.setBounds(10, 80, 100, 20);
        typeLabel.setBounds(80, 80, 100, 20);

        lastModifiy.setBounds(10, 110, 70, 20);
        lastModifiyText.setBounds(80, 110, 100, 20);

        majorText.setBounds(210, 50, 70, 20);
        majorLabel.setBounds(280, 50, 50, 20);
        minorText.setBounds(210, 80, 100, 20);
        minorLabel.setBounds(280, 80, 100, 20);
        patchText.setBounds(210, 110, 100, 20);
        patchLabel.setBounds(280, 110, 100, 20);

        ReadVersion.setBounds(10, 200, 130, Common.HEIGHT);
        ReadValue.setBounds(130, 200, 100, Common.HEIGHT);
        versionLabel.setBounds(10, 170, 200, Common.HEIGHT);

        pb.setBounds(10, 230, 300, 20);
        pb.setPreferredSize(new Dimension(680, 20));
        pb.setStringPainted(true);
        pb.setMinimum(0);
        pb.setMaximum(100);
        pb.setBackground(Color.white);
        pb.setForeground(Color.red);

        draperOriginalJSP.setBounds(340, 75, 130, 390);
        draperBeforeUpgradeJSP.setBounds(460, 75, 130, 390);
        draperAfterUpgradeJSP.setBounds(580, 75, 130, 390);
        upgradeInformationJSP.setBounds(10, 270, 300, 200);

        jcSinge.setBounds(500, 5, 90, 20);
        jbAuto.setBounds(600, 5, 100, 20);
        jlAutoCount.setBounds(600, 25, 100, 20);

        exit.setBounds(this.getWidth()-60-5, 5, 60, 20);

        jOne2Two.setBounds(680, 50, 100, 20);

        StyleUtils.setBtnBg(choosebt);
        StyleUtils.setBtnBg(choosebt2);
        StyleUtils.setBtnBg(updateToSelectButton);
        StyleUtils.setBtnBg(updateButton);
        StyleUtils.setObjextBg(jcSinge);
        StyleUtils.setBtnBg(ReadVersion);
        StyleUtils.setBtnBg(ReadValue);
        StyleUtils.setBtnBg(jbAuto);
        StyleUtils.setBtnBg(exit);
        StyleUtils.setBtnBg(jOne2Two);

        add(choosebt);
        add(choosebt2);
        add(updateToSelectButton);
        add(updateButton);
        add(jfDelay);
        add(loclFile);
        add(loclFile2);
        add(type);
        add(typeText);
        add(majorLabel);
        add(minorLabel);
        add(patchLabel);
        add(majorText);
        add(minorText);
        add(patchText);
        add(typeLabel);
        add(typehText);
        add(devBox);
        add(lastModifiy);
        add(lastModifiyText);

        add(ReadVersion);
//        add(ReadValue);
        add(versionLabel);

        add(pb);

        add(draperOriginalJSP);
        add(draperBeforeUpgradeJSP);
        add(draperAfterUpgradeJSP);
        add(upgradeInformationJSP);

        add(jcSinge);
        add(jbAuto);
        add(jlAutoCount);

        add(exit);

        add(jOne2Two);

        choosebt.addActionListener(this);
        choosebt2.addActionListener(this);
        updateToSelectButton.addActionListener(this);
        updateButton.addActionListener(this);
        ReadVersion.addActionListener(this);
        ReadValue.addActionListener(this);
        jbAuto.addActionListener(this);
        exit.addActionListener(this);
        jOne2Two.addActionListener(this);

        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mUpdatePresenter.cancelListener();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (choosebt.equals(e.getSource())) {
            mUpdatePresenter.choosebt();
        } else if (choosebt2.equals(e.getSource())) {
            mUpdatePresenter.choosebt2();
        } else if (updateToSelectButton.equals(e.getSource())) {
            if (mUpdatePresenter.getUpdateState()) {
                if (!initUpdate()) {
                    return;
                }
                showUpgradeInformation("init upgrade");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        mUpdatePresenter.updateToSelectButton();
                    }
                };
                new Thread(runnable).start();
            } else {
                Object[] options = {"Confirm"};
                JOptionPane.showOptionDialog(null, "Please wait for the upgrade to complete!", "Alert", 0, 0, null, options, 0);
            }
        } else if (updateButton.equals(e.getSource())) {
            if (mUpdatePresenter.getUpdateState()) {
                if (!initUpdate()) {
                    return;
                }
                showUpgradeInformation("init upgrade");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        mUpdatePresenter.updateButton();
                    }
                };
                new Thread(runnable).start();
            } else {
                Object[] options = {"Confirm"};
                JOptionPane.showOptionDialog(null, "Please wait for the upgrade to complete!", "Alert", 0, 0, null, options, 0);
            }
        } else if (ReadVersion.equals(e.getSource())) {
            String version = Public.readVersion(getdevBoxSelectedItem());
            updateVersionLabel(version);
        } else if (ReadValue.equals(e.getSource())) {
            String s = Public.matchStr(mUpdatePresenter.getFirmWareType(), "[A-Z](2)\\-+");
            s+=s;
//            mUpdatePresenter.ReadValue();
//            if (autoVersion == 1) {
//                autoVersion = 2;
//            } else if (autoVersion == 2) {
//                autoVersion = 1;
//            }
//            complete = true;
        } else if (jbAuto.equals(e.getSource())) {
            if(!initCycleUpdate()){
                return;
            }
            if ("自动循环".equals(jbAuto.getText().toString().trim())) {
                jbAuto.setText("关闭循环");
                isAuto = true;
                autoStart();
            } else {
                jbAuto.setText("自动循环");
                isAuto = false;
            }
        }else if(jOne2Two.equals(e.getSource())) {
            updateOne2Two();
        }else if(exit.equals(e.getSource())){
            mUpdatePresenter.cancelListener();
            dispose();
        }
    }

    /**
     * 升级前的初始化
     *
     * @return
     */
    private boolean initUpdate() {
        clearBeforeAndAfterDeviceVersion();
        if (getFileName() == null || "".equals(getFileName())) {
            Object[] options = {"Confirm"};
            JOptionPane.showOptionDialog(null, "Please select the firmware!", "Alert", 0, 0, null, options, 0);
            return false;
        }
        return true;
    }

    /**
     * 升级前的初始化
     *
     * @return
     */
    private boolean initCycleUpdate() {
        if (getFileName() == null || "".equals(getFileName()) || getFileName2() == null || "".equals(getFileName2())) {
            Object[] options = {"Confirm"};
            JOptionPane.showOptionDialog(null, "Please select the firmware!", "Alert", 0, 0, null, options, 0);
            return false;
        }
        return true;
    }

    @Override
    public void updateDevBox(List<RemoteDevice> remoteDevices) {
        for (RemoteDevice remoteDevice : remoteDevices) {
            devBox.addItem(remoteDevice);
        }
    }

    @Override
    public void updateDevBox(RemoteDevice remoteDevice) {
        Map<Integer, RemoteDevice> remoteDeviceMap = MyLocalDevice.getRemoteDeviceMap();
        RemoteDevice device = remoteDeviceMap.get(remoteDevice.getInstanceNumber());
        devBox.removeItem(device);
        devBox.addItem(remoteDevice);


    }

    @Override
    public void updateFileText(String text) {
        loclFile.setText(text);
    }

    @Override
    public void updateFileText2(String text) {
        loclFile2.setText(text);
    }

    @Override
    public void updateVersionLabel(String text) {
        versionLabel.setText(text);
    }

    @Override
    public void updateVersionAndType(FirmWareInformation firmWareInformation) {
        typeText.setText(firmWareInformation.getType());
        typeLabel.setText("" + firmWareInformation.getTypeNum());
        majorLabel.setText("" + firmWareInformation.getMajorNum());
        minorLabel.setText("" + firmWareInformation.getMinorNum());
        patchLabel.setText("" + firmWareInformation.getPatchNum());
    }

    @Override
    public void updateLastModify(String lastModify) {
        lastModifiyText.setText(lastModify);
    }

    @Override
    public void updateProgress(int per) {
        pb.setValue(per);
    }

    @Override
    public void showError(String str) {
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.YES_OPTION);
    }

    @Override
    public String getFileName() {
        return loclFile.getText();
    }

    private String getFileName2(){
        return loclFile2.getText();
    }

    @Override
    public RemoteDevice getdevBoxSelectedItem() {
        return (RemoteDevice) devBox.getSelectedItem();
    }

    @Override
    public int getDelay() {
        String delay = jfDelay.getText().toString().trim();
        return ("".equals(delay)) ? 0 : Integer.valueOf(delay);
    }

    DefaultListModel<String> odv = new DefaultListModel<>();

    @Override
    public void showOriginalDeviceVersion(String version) {
        showOriginal(version);
    }

    private void showOriginal(final String version) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    odv.addElement(version);
                    draperOriginal.setModel(odv);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    DefaultListModel<String> bdv = new DefaultListModel<>();
    @Override
    public void showBeforeDeviceVersion(String version) {
        showBefore(version);
    }

    private void showBefore(final String version){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    bdv.addElement(version);
                    draperBeforeUpgrade.setModel(bdv);
                    if (odv.getSize() == bdv.getSize()) {
                        mUpdatePresenter.findBeforeDevice();
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    DefaultListModel<String> adv = new DefaultListModel<>();
    @Override
    public void showAfterDeviceVersion(String version) {
        showAfter(version);
    }

    private void showAfter(final String version){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    adv.addElement(version);
                    draperAfterUpgrade.setModel(adv);
                    if (bdv.size() == (adv.size())) {
                        mUpdatePresenter.cancelListener();
//                        showUpgradeInformation("delete listener");
//                        showUpgradeInformation("upgrade successful");
                        showUpgradeInformation(Common.STEP_3_END);
                        if (autoVersion == 1) {
                            autoVersion = 2;
                        } else if (autoVersion == 2) {
                            autoVersion = 1;
                        }
                        complete = true;
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void clearBeforeAndAfterDeviceVersion() {
        odv.clear();
        bdv.clear();
        adv.clear();
        draperOriginal.setModel(odv);
        draperBeforeUpgrade.setModel(bdv);
        draperAfterUpgrade.setModel(adv);
    }

    DefaultListModel<String> udv = new DefaultListModel<>();

    @Override
    public void showUpgradeInformation(final String version) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                udv.addElement(version);
                upgradeInformation.setModel(udv);
                upgradeInformation.setSelectedIndex(udv.size() - 1);
                upgradeInformation.ensureIndexIsVisible(udv.size() - 1);
            }
        });

        //upgradeInformation.revalidate();
    }

    @Override
    public void showConfirmDialog(String str) {
        Object[] options = {"Confirm", "Cancel"};
        int i = JOptionPane.showOptionDialog(null, str, "Upgrade?", 0, 0, new ImageIcon(), options, 0);
        System.out.println(i);
        if (i == JOptionPane.OK_OPTION) {
            //devBox.removeAllItems();
            mUpdatePresenter.update(false);
        } else {
            mUpdatePresenter.update(true);
        }
    }

    @Override
    public int getOriginalSize() {
        return odv.getSize();
    }

    @Override
    public int getBeforeSize() {
        return bdv.getSize();
    }



    private boolean isAuto = false;
    private int autoVersion = 0;
    private boolean complete = true;
    private int mCount = 0;

    private void autoStart() {
        autoVersion = 1;
        complete = true;
        clearBeforeAndAfterDeviceVersion();
        new Thread(auto).start();
    }

    private Runnable auto = new Runnable() {
        @Override
        public void run() {
            while (isAuto) {
                try {
                    if (complete) {
                        complete = false;
                        Thread.sleep(2000);
                        mCount++;
                        jlAutoCount.setText("" + mCount);
                        clearBeforeAndAfterDeviceVersion();
                        if (jcSinge.isSelected()) {
                            if (autoVersion == 1) {
                                mUpdatePresenter.autoOneUpdate1();
                                System.out.println("autoOneVersion==1");
                            } else if (autoVersion == 2) {
                                mUpdatePresenter.autoOneUpdate2();
                                System.out.println("autoOneVersion==2");
                            }
                        } else {
                            if (autoVersion == 1) {
                                mUpdatePresenter.autoUpdate1();
                                System.out.println("autoVersion==1");
                            } else if (autoVersion == 2) {
                                mUpdatePresenter.autoUpdate2();
                                System.out.println("autoVersion==2");
                            }
                        }
                    }
                    System.out.println("auto");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void updateOne2Two() {
        if (mUpdatePresenter.getUpdateState()) {
            if (!initCycleUpdate()) {
                return;
            }
            autoVersion = 1;
            new Thread(one2Two).start();
        }else {
            Object[] options = {"Confirm"};
            JOptionPane.showOptionDialog(null, "Please wait for the upgrade to complete!", "Alert", 0, 0, null, options, 0);
        }
    }

    Runnable one2Two=new Runnable() {
        @Override
        public void run() {
            for(int i=0;i<3;i++){
                clearBeforeAndAfterDeviceVersion();
                complete = false;
                if (autoVersion == 1) {
                    mUpdatePresenter.autoUpdate1();
                    System.out.println("autoVersion==1");
                } else if (autoVersion == 2) {
                    mUpdatePresenter.autoUpdate2();
                    System.out.println("autoVersion==2");
                }
                while (!complete){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            autoVersion = 0;
        }
    };
}

