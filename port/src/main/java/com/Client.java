package com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Client extends JFrame implements ActionListener {
    JTextArea input;
    JTextArea output;
    JButton close;
    JButton send;
    JScrollPane top = new JScrollPane();
    JPanel medium, bottom;

    String inMessage, outMessage;

    String s = null;
    Socket mysocket;
    BufferedReader br = null;
    PrintWriter pw = null;

    /**
     * 36      * @param args
     * 37
     */
    public Client() {
        super();
        setTitle("客户端");
        setVisible(true);
        setBounds(200, 150, 350, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        top = new JScrollPane();
        top.setPreferredSize(new Dimension(300, 200));
        top.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        output = new JTextArea(6, 25);
        top.setViewportView(output);

        medium = new JPanel();
        medium.setPreferredSize(new Dimension(300, 120));
        input = new JTextArea(4, 27);
        medium.add(input);

        bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(300, 60));
        close = new JButton("关闭");
        close.addActionListener(this);
        send = new JButton("发送");
        send.addActionListener(this);
        bottom.add(close);
        bottom.add(send);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(medium, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Client client = new Client();
        client.run();
    }

    public void run() {

        try {
            mysocket = new Socket("127.0.0.1", 2222);
            br = new BufferedReader(new InputStreamReader(
                    mysocket.getInputStream()));
            pw = new PrintWriter(mysocket.getOutputStream(), true);
//            while ((inMessage = br.readLine()) != null) {
//                output.append("服务器说：" + inMessage + "\n");
//            }
            int read = br.read();
            while ((read = br.read()) != 0) {
                output.append("服务器说：" + inMessage + "\n");
            }
//            int read = br.read();
        } catch (Exception e) {
        }
    }


    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (close.hasFocus()) {
            try {
                mysocket.close();
                System.out.println("客户端已关闭");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if (send.hasFocus()) {
            outMessage = input.getText();
            input.setText(null);
            output.append("客户端说：" + outMessage + "\n");
            pw.println(outMessage);
            pw.flush();
        }
    }

    class Test{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
