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


        add(draperOriginalJSP);
        add(draperBeforeUpgradeJSP);
        add(draperAfterUpgradeJSP);

        choosebt.addActionListener(this);
        updateToSelectButton.addActionListener(this);
        updateButton.addActionListener(this);
        ReadVersion.addActionListener(this);
        ReadValue.addActionListener(this);



        setResizable(false);
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
        }
    }



    Timer timer = new Timer(1, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (true) {

            }
            System.out.println("start");
        }
    });

    /**
     * 升级前的初始化
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
