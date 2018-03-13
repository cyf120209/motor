
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Java GUI program to set static IP parameters in /etc/dhcpcd.conf
 *
 * @author Knute Johnson
 * @version 0.1beta
 */
public class IPConfig extends JPanel {
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
    private final JTextField eth0AddressField;

    /** eth0 routers field */
    private final JTextField eth0RoutersField;

    /** eth0 domain name servers field */
    private final JTextField eth0NameServersField;

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

    /**
     * Creates a new IPConfig GUI
     *
     * @throws IOException if an I/O error occurs reading the dhcpcd.conf file
     */
    public IPConfig() throws IOException {
        super(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2,2,2,2);
        c.gridx = c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridwidth = 2;  c.anchor = GridBagConstraints.CENTER;
        JLabel l = new JLabel("eth0",JLabel.CENTER);
        add(l,c);

        ++c.gridy; c.gridwidth = 1;  c.anchor = GridBagConstraints.WEST;
        l = new JLabel("IP Address");
        add(l,c);

        ++c.gridx;
        eth0AddressField = new JTextField(15);
        add(eth0AddressField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Gateway");
        add(l,c);

        ++c.gridx;
        eth0RoutersField = new JTextField(15);
        add(eth0RoutersField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Name Servers");
        add(l,c);

        ++c.gridx;
        eth0NameServersField = new JTextField(15);
        add(eth0NameServersField,c);

        c.gridx = 0;  ++c.gridy;
        c.gridwidth = 2;  c.anchor = GridBagConstraints.CENTER;
        l = new JLabel("wlan0",JLabel.CENTER);
        add(l,c);

        ++c.gridy; c.gridwidth = 1;  c.anchor = GridBagConstraints.WEST;
        l = new JLabel("IP Address");
        add(l,c);

        ++c.gridx;
        wlan0AddressField = new JTextField(15);
//        wlan0AddressField.setText("192.168.20.45/24");
        add(wlan0AddressField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Gateway");
        add(l,c);

        ++c.gridx;
        wlan0RoutersField = new JTextField(15);
//        wlan0RoutersField.setText("192.168.20.1");
        add(wlan0RoutersField,c);

        c.gridx = 0;  ++c.gridy;
        l = new JLabel("Name Servers");
        add(l,c);

        ++c.gridx;
        wlan0NameServersField = new JTextField(15);
//        wlan0NameServersField.setText("192.168.20.5 218.85.152.99");
        add(wlan0NameServersField,c);

        c.gridx = 0;  ++c.gridy;  c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        JButton b = new JButton("Save");
        b.addActionListener(event -> {
            depopulateFields();
            System.out.println("depopulateFields");
            try {
                saveConfFile();
                System.out.println("saveConfFile");
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(IPConfig.this,ioe,
                        "Error saving dhcpcd.conf file",JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
            System.out.println("finish");
        });
        add(b,c);
        loadConfFile();
        populateFields();
    }

    /**
     * Loads the dhcpcd.conf file data into the LINES array list
     *
     * @throws IOException if an I/O error occurs reading the dhcpcd.conf file
     */
    private void loadConfFile() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(CONF_FILE))) {
            String str;
            while ((str = br.readLine()) != null)
                LINES.add(str);
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
        InputStream is = p.getInputStream();
        new Thread(() -> {
            int c;
            try {
                while ((c = is.read()) != -1)
                    System.out.print((char)c);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();
    }

    /**
     * Populates the GUI data fields with the appropriate data from the
     * dhcpcd.conf file
     */
    private void populateFields() {
        // find eth0 data
        for (int i=0; i<LINES.size(); i++) {
            if (LINES.get(i).trim().equals("interface eth0")) {
                eth0 = i;
                if (LINES.get(i+1).trim().startsWith("static ip_address=")) {
                    String arr[] = LINES.get(i+1).split("=");
                    eth0AddressField.setText(arr[1]);
                }
                if (LINES.get(i+2).trim().startsWith("static routers=")) {
                    String arr[] = LINES.get(i+2).split("=");
                    eth0RoutersField.setText(arr[1]);
                }
                if (LINES.get(i+3).trim().startsWith(
                        "static domain_name_servers=")) {
                    String arr[] = LINES.get(i+3).split("=");
                    eth0NameServersField.setText(arr[1]);
                }
            }

            // find wlan0 data
            if (LINES.get(i).trim().equals("interface wlan0")) {
                wlan0 = i;
                if (LINES.get(i+1).trim().startsWith("static ip_address=")) {
                    String arr[] = LINES.get(i+1).split("=");
                    wlan0AddressField.setText(arr[1]);
                }
                if (LINES.get(i+2).trim().startsWith("static routers=")) {
                    String arr[] = LINES.get(i+2).split("=");
                    wlan0RoutersField.setText(arr[1]);
                }
                if (LINES.get(i+3).trim().startsWith(
                        "static domain_name_servers=")) {
                    String arr[] = LINES.get(i+3).split("=");
                    wlan0NameServersField.setText(arr[1]);
                }
            }
        }
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

    /**
     * Main program entry point, creates the containing frame and an IPConfig
     * GUI
     */
    public static void main(String... args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame f = new JFrame("IPConfig " + VERSION);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                IPConfig i = new IPConfig();
                f.add(i,BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null,ioe,
                        "Error loading dhcpcd.conf file",JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
        });
    }
}