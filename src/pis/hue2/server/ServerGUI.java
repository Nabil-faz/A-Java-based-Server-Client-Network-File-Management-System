package pis.hue2.server;

import pis.hue2.common.msg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class ServerGUI {

    static JTextArea chat_history=new JTextArea(80,80);
    final JTextField msg=new JTextField();
    JScrollPane scroll;
    JButton send,startServer,stopServer;
    msg c=new msg();
    LaunchServer ls;
    public static JFrame gui;

    /**
     * Initialize the GUI interface and runs it including all button listener
     */

    public void createGUI(){
        gui=new JFrame("Server");

        ls=new LaunchServer();

        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chat_history.setEditable(false);
        chat_history.setBounds(50,30,480,450);

        scroll=new JScrollPane(chat_history);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(50,30,480,450);

        msg.setBounds(50,500,480,60);
        gui.add(msg);

        send=new JButton("Send");
        send.setBounds(550,500,80,40);

        startServer=new JButton("Start Server");
        startServer.setBounds(550,400,120,40);

        stopServer=new JButton("Close Server");
        stopServer.setBounds(550,350,120,40);


        gui.add(scroll);
        gui.add(send);
        gui.add(startServer);
        gui.add(stopServer);

        gui.setSize(750,700);
        gui.setLayout(null);
        gui.setVisible(true);

        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LaunchServer.ss==null||LaunchServer.ss.isClosed()) {
                    chat_history.setText("Server is open on port " + 4999 + "\nWaiting for clients to connect \n");
                    ls.startServer();
                }else{
                    JOptionPane.showMessageDialog(null, "Server is already running");
                }
            }
        });

        stopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LaunchServer.ss!=null){
                    if (!LaunchServer.ss.isClosed() && LaunchServer.socketList.size()>0) {
                        for (Socket s:LaunchServer.socketList ) {
                            if (!s.isClosed()) {
                                c.send(s, "DSC" + " " + "Server-> Server is Closing , disconnecting all clients ....");
                            }
                        }
                        showMSG("Server is Closing , disconnecting all clients ");
                    }
                }
                ls.stopServer();

            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LaunchServer.socketList.size()==0){
                    JOptionPane.showMessageDialog(null, "There are no clients available at the moment");
                    msg.setText("");
                }else {
                    for (Socket s:LaunchServer.socketList ) {
                        if (!s.isClosed()) {
                            c.send(s, "Server-> " + msg.getText());
                        }
                    }
                    showMSG("Server-> " + msg.getText());
                    msg.setText("");
                }
            }
        });
    }

    /**
     * Displays text in the textarea
     * @param msg the string to display
     */

    public void showMSG(String msg){
        chat_history.setText(chat_history.getText().trim() + "\n"+ msg);
    }

}
