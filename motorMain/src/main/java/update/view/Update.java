package update.view;

import com.serotonin.bacnet4j.RemoteDevice;
import common.Common;
import update.presenter.UpdatePresenter;
import update.presenter.UpdatePresenterImpl;
import util.Draper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by lenovo on 2017/1/20.
 */
public class Update extends JFrame implements UpdateView, ActionListener {

    private final UpdatePresenter mUpdatePresenter;
    private final Thread thread;
    public JButton choosebt = new JButton("choose file");
    public JButton updateToSelectButton = new JButton("Update select");
    public JButton updateButton = new JButton("Update");

    public JLabel loclFile = new JLabel("");
    public JLabel lastModifiy = new JLabel("lastModifiy:");
    public JLabel type = new JLabel("Type:");
    public JLabel majorText = new JLabel("Major Num:");
    public JLabel minorText = new JLabel("Minor Num:");
    public JLabel patchText = new JLabel("Patch Num:");
    public JLabel typehText = new JLabel("type Num:");
    public JLabel lastModifiyText = new JLabel("");
    public JLabel typeText = new JLabel("");
    public JLabel majorLabel = new JLabel("");
    public JLabel minorLabel = new JLabel("");
    public JLabel patchLabel = new JLabel("");
    public JLabel typeLabel = new JLabel("");
    public JComboBox devBox = new JComboBox();

    public JLabel versionLabel = new JLabel("Versin: No Version");
    public JButton ReadVersion = new JButton("readVersion");
    public JButton ReadValue = new JButton("readValue");

    private JProgressBar pb = new JProgressBar();


    private JTextField jf1 = new JTextField();
    private JTextField jf2 = new JTextField();
    private JTextField jf3 = new JTextField();
    private JTextField jf4 = new JTextField();
    private JTextField jfTime = new JTextField();
    private JTextField jfInterval = new JTextField();
    private JButton jbStart = new JButton("开始");
    private JLabel jlCount = new JLabel("次数");

    public JList draperOriginal = new JList();
    JScrollPane draperOriginalJSP = new JScrollPane(draperOriginal);

    public JList draperBeforeUpgrade = new JList();
    JScrollPane draperBeforeUpgradeJSP = new JScrollPane(draperBeforeUpgrade);

    public JList draperAfterUpgrade = new JList();
    JScrollPane draperAfterUpgradeJSP = new JScrollPane(draperAfterUpgrade);


    public Update() throws HeadlessException {
        mUpdatePresenter = new UpdatePresenterImpl(this);

        setTitle("update");
        setLayout(null);
        setSize(700, 500);

        loclFile.setBounds(10, 25, 500, 20);

        choosebt.setBounds(new Rectangle(10, 5, 100, 20));
        updateButton.setBounds(240, 5, 100, 20);
        updateToSelectButton.setBounds(110, 5, 130, 20);
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

        ReadVersion.setBounds(10, 200, 120, Common.HEIGHT);
        ReadValue.setBounds(130, 200, 100, Common.HEIGHT);
        versionLabel.setBounds(10, 170, 200, Common.HEIGHT);

        pb.setBounds(10, 230, 300, 20);
        pb.setPreferredSize(new Dimension(680, 20));
        pb.setStringPainted(true);
        pb.setMinimum(0);
        pb.setMaximum(100);
        pb.setBackground(Color.white);
        pb.setForeground(Color.red);

        jf1.setBounds(10, 300, 200, 20);
        jf2.setBounds(10, 320, 200, 20);
        jf3.setBounds(10, 340, 200, 20);
        jf4.setBounds(10, 360, 200, 20);
        jfTime.setBounds(220, 300, 100, 20);
        jfInterval.setBounds(220, 320, 100, 20);
        jbStart.setBounds(220, 340, 100, 20);
        jlCount.setBounds(220, 360, 100, 20);


        draperOriginalJSP.setBounds(340, 40, 110, 400);
        draperBeforeUpgradeJSP.setBounds(460, 40, 110, 400);
        draperAfterUpgradeJSP.setBounds(580, 40, 110, 400);

        add(choosebt);
        add(updateToSelectButton);
        add(updateButton);
        add(loclFile);
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
        add(ReadValue);
        add(versionLabel);

        add(pb);

        add(jf1);
        add(jf2);
        add(jf3);
        add(jf4);
        add(jfTime);
        add(jfInterval);
        add(jbStart);
        add(jlCount);

        add(draperOriginalJSP);
        add(draperBeforeUpgradeJSP);
        add(draperAfterUpgradeJSP);

        choosebt.addActionListener(this);
        updateToSelectButton.addActionListener(this);
        updateButton.addActionListener(this);
        ReadVersion.addActionListener(this);
        ReadValue.addActionListener(this);

        jbStart.addActionListener(this);

        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isRunning=false;
                mUpdatePresenter.cancelListener();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                send();
            }
        });
        thread.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (choosebt.equals(e.getSource())) {
            mUpdatePresenter.choosebt();
        } else if (updateToSelectButton.equals(e.getSource())) {
            if (!initUpdate()) {
                return;
            }
            if (mUpdatePresenter.getUpdateState()) {
                mUpdatePresenter.updateToSelectButton();
            } else {
                Object[] options = {"Confirm"};
                JOptionPane.showOptionDialog(null, "Please wait for the upgrade to complete!", "Alert", 0, 0, null, options, 0);
            }
        } else if (updateButton.equals(e.getSource())) {
            if (!initUpdate()) {
                return;
            }
            if (mUpdatePresenter.getUpdateState()) {
                mUpdatePresenter.updateButton();
            } else {
                Object[] options = {"Confirm"};
                JOptionPane.showOptionDialog(null, "Please wait for the upgrade to complete!", "Alert", 0, 0, null, options, 0);
            }
        } else if (ReadVersion.equals(e.getSource())) {
            String version = mUpdatePresenter.ReadVersion(getdevBoxSelectedItem());
            updateVersionLabel(version);
        } else if (ReadValue.equals(e.getSource())) {
            mUpdatePresenter.ReadValue();
        } else if (jbStart.equals(e.getSource())) {
            if (jbStart.getText().equals("开始")) {
                jbStart.setText("暂停");
                start();
            } else {
                jbStart.setText("开始");
                stop();
            }
        }
    }

    private void start() {
//        time.start();
        count=0;
        time = Integer.valueOf(jfTime.getText().toString().trim());
        isLoop = true;
    }

    private void stop() {
//        time.stop();
        isLoop = false;
    }

    boolean isLoop = false;
    boolean isRunning = true;
    int time=0;
    int count=0;

    private void send() {
        while (isRunning) {
            try {
//            System.out.println("-------isLoop");
                Thread.sleep(10);
                if (isLoop) {
                    count++;
                    jlCount.setText(""+count);
//            byte[] byte1 = Byte2IntUtils.hexStringToBytes(jf1.getText().toString().trim());
                    Integer cmd1 = Integer.valueOf(jf1.getText().toString().trim());
                    Integer cmd2 = Integer.valueOf(jf2.getText().toString().trim());
                    Integer cmd3 = Integer.valueOf(jf3.getText().toString().trim());
                    Integer cmd4 = Integer.valueOf(jf4.getText().toString().trim());

                    Draper.sendCmd(cmd1);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    Draper.sendCmd(cmd2);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    Draper.sendCmd(cmd3);
                    Thread.sleep(time);
                    if(!isLoop) continue;
                    Draper.sendCmd(cmd4);
                    Thread.sleep(Integer.valueOf(jfInterval.getText().toString().trim()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    Timer timer = new Timer(1, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (true) {

            }
            System.out.println("start");
        }
    });


    private boolean initUpdate() {
        clearBeforeAndAfterDeviceVersion();
        if (getFileName() == null || "".equals(getFileName())) {
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
        devBox.addItem(remoteDevice);
    }

    @Override
    public void updateFileText(String text) {
        loclFile.setText(text);
    }

    @Override
    public void updateTypeLabelText(String text) {
        typeLabel.setText(text);
    }

    @Override
    public void updateVersionLabel(String text) {
        versionLabel.setText(text);
    }

    @Override
    public void updateVersionAndType(String type, String major, String minor, String patch) {
        typeText.setText(type);
        majorLabel.setText(major);
        minorLabel.setText(minor);
        patchLabel.setText(patch);
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

    @Override
    public int getMajorNum() {
        return Integer.parseInt(majorLabel.getText().trim());
    }

    @Override
    public int getMinorNum() {
        return Integer.parseInt(minorLabel.getText().trim());
    }

    @Override
    public int getPatchNum() {
        return Integer.parseInt(patchLabel.getText().trim());
    }

    @Override
    public int getTypeNum() {
        return Integer.parseInt(typeLabel.getText().trim());
    }

    @Override
    public RemoteDevice getdevBoxSelectedItem() {
        return (RemoteDevice) devBox.getSelectedItem();
    }


    DefaultListModel<String> odv = new DefaultListModel<>();

    @Override
    public void showOriginalDeviceVersion(String version) {
        odv.addElement(version);
        draperOriginal.setModel(odv);
    }

    DefaultListModel<String> bdv = new DefaultListModel<>();

    @Override
    public void showBeforeDeviceVersion(String version) {
        bdv.addElement(version);
        draperBeforeUpgrade.setModel(bdv);
        if (odv.getSize() == bdv.getSize()) {
            mUpdatePresenter.findAllDevice();
        }
    }

    DefaultListModel<String> adv = new DefaultListModel<>();

    @Override
    public void showAfterDeviceVersion(String version) {
        adv.addElement(version);
        draperAfterUpgrade.setModel(adv);
    }

    public void clearBeforeAndAfterDeviceVersion() {
        bdv.clear();
        adv.clear();
        draperBeforeUpgrade.setModel(bdv);
        draperAfterUpgrade.setModel(adv);
    }

    @Override
    public void showConfirmDialog() {
        Object[] options = {"Confirm", "Cancel"};
        int i = JOptionPane.showOptionDialog(null, "Upgrade?", "Upgrade?", 0, 0, new ImageIcon(), options, 0);
        System.out.println(i);
        if (i == JOptionPane.OK_OPTION) {
            devBox.removeAllItems();
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
}
