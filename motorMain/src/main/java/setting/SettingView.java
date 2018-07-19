package setting;

import common.Common;
import util.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class SettingView extends JFrame implements ActionListener {

    /** Program version */
    public static String VERSION = "0.1beta";

    /** dhcpcd.conf file */
    private final File CONF_FILE = new File("/etc/dhcpcd.conf");

    /** Temporary file */
    private final File TEMP_FILE = new File(
            System.getProperty("user.home"),"temp");

    /** Script file */
    private final File SCRIPT_FILE = new File(
            System.getProperty("user.home"),"ipconfig-script");

    /** List to hold lines from dhcpcd.conf file */
    private final java.util.List<String> LINES = new ArrayList<>();

    /** eth0 address field */
    private JTextField eth0AddressField;

    /** eth0 routers field */
    private JTextField eth0RoutersField;

    /** eth0 domain name servers field */
    private JTextField eth0NameServersField;

    /** wlan0 address field */
    private final JTextField wlan0AddressField;

    /** wlan0 routers field */
    private final JTextField wlan0RoutersField;

    /** wlan0 domain name servers field */
    private final JTextField wlan0NameServersField;

    /** index of interface eth0 line */
    private int eth0;

    /** index of interface wlan0 line */
    private int wlan0;


    public SettingView() throws HeadlessException {

        setTitle("Suntracking");
        setSize(Common.SCREEN_WEIGHT, Common.SCREEN_HEIGHT);

        setLocationRelativeTo(this);

        setLayout(null);
        //总在最前面
//        setAlwaysOnTop(true);
        //不能改变大小
        setResizable(false);
        //最大化
//        setExtendedState(JFrame.MAXIMIZED_BOTH);

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

        Panel ipPanel = new Panel();
        ipPanel.setLayout(new GridBagLayout());
//        ipPanel.setBackground(Color.cyan);
//        ipPanel.setSize(600,400);
        ipPanel.setBounds(0,0,300,200);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2,2,2,2);
        c.gridx = c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel l = new JLabel("",JLabel.CENTER);
//        JLabel l = new JLabel("eth0",JLabel.CENTER);
//        ipPanel.add(l,c);
//
//        ++c.gridy; c.gridwidth = 1;  c.anchor = GridBagConstraints.WEST;
//        l = new JLabel("IP Address");
//        ipPanel.add(l,c);
//
//        ++c.gridx;
//        eth0AddressField = new JTextField(15);
//        ipPanel.add(eth0AddressField,c);
//
//        c.gridx = 0;  ++c.gridy;
//        l = new JLabel("Gateway");
//        ipPanel.add(l,c);
//
//        ++c.gridx;
//        eth0RoutersField = new JTextField(15);
//        ipPanel.add(eth0RoutersField,c);
//
//        c.gridx = 0;  ++c.gridy;
//        l = new JLabel("Name Servers");
//        ipPanel.add(l,c);
//
//        ++c.gridx;
//        eth0NameServersField = new JTextField(15);
//        ipPanel.add(eth0NameServersField,c);

        c.gridx = 0;  ++c.gridy;
        c.gridwidth = 2;  c.anchor = GridBagConstraints.CENTER;
        l = new JLabel("wlan0",JLabel.CENTER);
        ipPanel.add(l,c);

        ++c.gridy; c.gridwidth = 1;  c.anchor = GridBagConstraints.WEST;
        l = new JLabel("IP Address");
        ipPanel.add(l,c);

        ++c.gridx;
        wlan0AddressField = new JTextField(15);
//        wlan0AddressField.setText("192.168.20.45/24");
        ipPanel.add(wlan0AddressField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Gateway");
        ipPanel.add(l,c);

        ++c.gridx;
        wlan0RoutersField = new JTextField(15);
//        wlan0RoutersField.setText("192.168.20.1");
        ipPanel.add(wlan0RoutersField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Name Servers");
        ipPanel.add(l,c);

        ++c.gridx;
        wlan0NameServersField = new JTextField(15);
//        wlan0NameServersField.setText("192.168.20.5 218.85.152.99");
        ipPanel.add(wlan0NameServersField,c);

        c.gridx = 0;  ++c.gridy;  c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        JButton b = new JButton("Save");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depopulateFields();
                System.out.println("depopulateFields");
                try {
                    saveConfFile();
                    System.out.println("saveConfFile");
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(SettingView.this,ioe,
                            "Error saving dhcpcd.conf file",JOptionPane.ERROR_MESSAGE);
                    ioe.printStackTrace();
                }
                System.out.println("finish");
            }
        });
        ipPanel.add(b);
        add(ipPanel);

    }

    /**
     * Depopulates the data fields and stores the data in the LINES array list
     */
    private void depopulateFields() {
        eth0 = wlan0 = 0;
        // find eth0
        for (int i=0; i<LINES.size(); i++) {
            if (LINES.get(i).trim().startsWith("interface eth0"))
                eth0 = i;
        }
        // if there is no eth0 data add it
        if (eth0 == 0) {
            eth0 = LINES.size();
            LINES.add("interface eth0");
            LINES.add(eth0 + 1,
                    "static ip_address=" + eth0AddressField.getText());
            LINES.add(eth0 + 2,"static routers=" + eth0RoutersField.getText());
            LINES.add(eth0 + 3,
                    "static domain_name_servers=" + eth0NameServersField.getText());
            // else modify it
        } else {
            LINES.set(eth0 + 1,
                    "static ip_address=" + eth0AddressField.getText());
            LINES.set(eth0 + 2,"static routers=" + eth0RoutersField.getText());
            LINES.set(eth0 + 3,
                    "static domain_name_servers=" + eth0NameServersField.getText());
        }

        // find wlan0
        for (int i=0; i<LINES.size(); i++) {
            if (LINES.get(i).trim().startsWith("interface wlan0"))
                wlan0 = i;
        }
        // if there is no wlan0 data add it
        if (wlan0 == 0) {
            wlan0 = LINES.size();
            LINES.add("interface wlan0");
            LINES.add(wlan0 + 1,
                    "static ip_address=" + wlan0AddressField.getText());
            LINES.add(wlan0 + 2,
                    "static routers=" + wlan0RoutersField.getText());
            LINES.add(wlan0 + 3,
                    "static domain_name_servers=" + wlan0NameServersField.getText());
            // else modify it
        } else {
            LINES.set(wlan0 + 1,
                    "static ip_address=" + wlan0AddressField.getText());
            LINES.set(wlan0 + 2,
                    "static routers=" + wlan0RoutersField.getText());
            LINES.set(wlan0 + 3,
                    "static domain_name_servers=" + wlan0NameServersField.getText());
        }

        // find wlan0 data in the LINES array list
        for (int i=0; i<LINES.size(); i++) {
            if (LINES.get(i).trim().startsWith("interface wlan0"))
                wlan0 = i;
        }
        // if the wlan0 address field has been cleared
        if (wlan0AddressField.getText().equals("")) {
            // remove the wlan0 data
            for (int i=wlan0; i<wlan0 + 4; i++)
                LINES.remove(wlan0);
        }

        // find the eth0 data in the LINES array list
        for (int i=0; i<LINES.size(); i++) {
            if (LINES.get(i).trim().startsWith("interface eth0"))
                eth0 = i;
        }
        // if the eth0 address field has been cleared
        if (eth0AddressField.getText().equals("")) {
            // remove the eth0 data
            for (int i=eth0; i<eth0 + 4; i++)
                LINES.remove(eth0);
        }
    }

    /**
     * Saves the modified data to the dhcpcd.conf file
     *
     * @throws IOException if an I/O error occurs saving the data
     */
    private void saveConfFile() throws IOException {
        // create the temporary file
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(TEMP_FILE))) {
            for (String line : LINES) {
                bw.write(line);
                bw.newLine();
//                System.out.println("line: "+line);
            }
        }
//        try (BufferedReader br = new BufferedReader(
//                new FileReader(TEMP_FILE))) {
//            String str;
//            while ((str = br.readLine()) != null)
//                System.out.println(str);
//        }

        // create the script file
        System.out.println("createScriptFile start");
        createScriptFile();
        System.out.println("createScriptFile finish");
        // run the script and capture stdout and stderr to the terminal
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.home")+"/ipconfig-script");
        pb.redirectErrorStream();
        Process p = pb.start();
        final InputStream is = p.getInputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int c;
                try {
                    while ((c = is.read()) != -1)
                        System.out.print((char)c);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Creates the script file that will prep and copy the temporary file to
     * the /etc/dhcpcd.conf file
     *
     * @throws IOException if an I/O error occurs during file creation
     */
    private void createScriptFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(SCRIPT_FILE))) {
            bw.write("#!/bin/bash");
            bw.newLine();
            bw.write("chmod 664 $HOME/temp");
            bw.newLine();
            bw.write("sudo chown root:netdev $HOME/temp");
            bw.newLine();
            bw.write("sudo mv $HOME/temp /etc/dhcpcd.conf");
            bw.newLine();
            bw.write("echo ipconfig-script complete!");
            bw.newLine();
        }
        SCRIPT_FILE.setExecutable(true);
        SCRIPT_FILE.deleteOnExit();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
