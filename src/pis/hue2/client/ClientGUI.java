package pis.hue2.client;

import pis.hue2.common.User;
import pis.hue2.common.files;
import pis.hue2.common.msg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;


public class ClientGUI {

    JTextArea chat_history=new JTextArea();
    final JTextField MSG=new JTextField();
    JTextArea textArea = new JTextArea();
    public static JFileChooser fileChooser;
    JScrollPane scroll;
    JPanel container,nameP, info,portP,ipP,infoP,buttonP;
    JTextField name,ip,port;
    JLabel nameL,ipL,portL,serverInfo;
    JButton send, con, put,get,list,del, dsc;
    JFrame pop_gui;

    static File file;
    static String lst;
    files f=new files();
    String stat;


    /**
     * Initializes the client welcome window where the client can enter their name , Server ip and port .
     */
    public void welcomeWindow(){
        JFrame gui=new JFrame("Client");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        name=new JTextField(20);
        name.setText("Client");

        ip=new JTextField(20);
        ip.setText("127.0.0.1");

        port=new JTextField(20);
        port.setText(Integer.toString(4999));

        nameL=new JLabel("Name:   ");
        nameL.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));

        ipL=new JLabel("IP:         ");
        ipL.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));

        portL=new JLabel("Port:    ");
        portL.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));

        nameP=new JPanel(new FlowLayout(FlowLayout.CENTER));
        nameP.add(nameL);
        nameP.add(name);

        portP=new JPanel(new FlowLayout(FlowLayout.CENTER));
        portP.add(portL);
        portP.add(port);

        ipP=new JPanel(new FlowLayout(FlowLayout.CENTER));
        ipP.add(ipL);
        ipP.add(ip);

        serverInfo=new JLabel("Server information");
        serverInfo.setFont(new Font(Font.SANS_SERIF,Font.BOLD,22));

        infoP=new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoP.add(serverInfo);

        con =new JButton("Start");

        buttonP=new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonP.add(con);


        info=new JPanel();
        info.setSize(200,300);
        info.add(infoP);
        info.add(nameP);
        info.add(ipP);
        info.add(portP);


        container=new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(info);
        container.add(buttonP);


        gui.setSize(400,330);
        gui.setAlwaysOnTop(true);
        gui.setContentPane(container);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);

        con.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientGUI g=new ClientGUI();
                gui.dispose();

                Socket s = null;
                User clientData=new User(name.getText(),s,ip.getText(),Integer.parseInt(port.getText()));
                LaunchClient client=new LaunchClient(clientData,g);
                g.createGUI(clientData,client);
            }
        });



    }

    /**
     * Prints the given text to the GUI message/text area
     * @param msg text to be printed
     */
    public void showMSG(String msg){
        chat_history.setText(chat_history.getText().trim() + "\n"+ msg);
    }
    /**
     * Prints the given text to the pop-out_GUI text area
     * @param msg text to be printed
     */
    public void pop_showMSG(String msg){
        textArea.setText(textArea.getText().trim() + "\n"+ msg);
    }

    /**
     * Initializes the client GUI and runs it including all button listener
     * @param client the client connected to the server
     * @param lc
     */
    public void createGUI(User client,LaunchClient lc){

        JFrame gui=new JFrame("Client");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pop_gui=new JFrame("Server Files");
        pop_gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        msg c=new msg();

        chat_history.setBounds(50,30,480,450);
        chat_history.setEditable(false);

        scroll=new JScrollPane(chat_history);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(50,30,480,450);

        MSG.setBounds(50,500,480,60);

        send=new JButton("Send");
        send.setBounds(550,500,80,35);

        put =new JButton("PUT");
        put.setBounds(550,450,80,35);

        get =new JButton("GET");
        get.setBounds(550,400,80,35);

        list =new JButton("LIST");
        list.setBounds(550,350,80,35);

        del =new JButton("DEL");
        del.setBounds(550,300,80,35);

        dsc =new JButton("Disconnect");
        dsc.setBounds(550,100,100,35);

        con =new JButton("Connect");
        con.setBounds(550,150,100,35);


        textArea.setBounds(10, 11, 609, 264);
        textArea.setEditable(false);
        pop_gui.getContentPane().add(textArea);

        JTextField textField = new JTextField();
        textField.setBounds(10, 286, 609, 31);
        pop_gui.getContentPane().add(textField);
        textField.setColumns(10);

        JButton popBtn = new JButton("send");
        popBtn.setBounds(629, 274, 96, 43);
        pop_gui.getContentPane().add(popBtn);

        pop_gui.setBounds(100, 100, 751, 367);
        pop_gui.getContentPane().setLayout(null);
        pop_gui.setAlwaysOnTop(true);
        pop_gui.setVisible(false);


        gui.add(scroll);
        gui.add(MSG);
        gui.add(send);
        gui.add(put);
        gui.add(get);
        gui.add(list);
        gui.add(del);
        gui.add(dsc);
        gui.add(con);
        gui.setSize(750,700);
        gui.setLayout(null);
        gui.setVisible(true);

        String home=System.getProperty("user.home");

        popBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    boolean found = Arrays.asList(lst.split("  ")).contains(textField.getText());
                    if (found) {
                        if (Objects.equals(popBtn.getText(), "send")) {
                            stat="GET";
                            c.send(client.getS(), "GET" + " " + textField.getText());
                        } else {
                            stat="DEL";
                            c.send(client.getS(), "DEL" + " " + textField.getText());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "file not found");
                    }
                pop_gui.dispose();
                textField.setText("");
                textArea.setText("");
                MSG.setEditable(true);
                pop_gui.setAlwaysOnTop(false);
            }
        });

        pop_gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                MSG.setEditable(true);
                pop_gui.setAlwaysOnTop(false);
                textArea.setText("");
            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m= client.getName()+"-> "+MSG.getText();
                if (!client.isStatus()){
                    JOptionPane.showMessageDialog(null, "Connect to server!");
                }else {
                    c.send(client.getS(),m);
                    SwingUtilities.invokeLater(()->{ showMSG(m);});
                }
                MSG.setText("");

            }
        });
        put.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isStatus()){
                    JOptionPane.showMessageDialog(null, "Connect to server!");
                }else {
                    file = f.chooseFile(ClientGUI.fileChooser);
                    if (file != null) {
                        stat="PUT";
                        c.send(client.getS(), "PUT"+" "+file.getName());
                    }
                }
            }
        });

        get.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isStatus()){
                    JOptionPane.showMessageDialog(null, "Connect to server!");
                }else {
                    lst=f.listFiles(  home+"\\Desktop\\ServerFiles");
                    if ((Objects.equals(lst, ""))) {
                        JOptionPane.showMessageDialog(null, "Server is empty");
                    }else{
                    MSG.setEditable(false);
                    popBtn.setText("send");
                    pop_gui.setVisible(true);
                    pop_gui.setAlwaysOnTop(true);
                    stat="LST";
                    c.send(client.getS(), "LST");
                    }
                }
            }
        });

        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isStatus()){
                    JOptionPane.showMessageDialog(null, "Connect to server!");
                }else {
                    lst=f.listFiles(  home+"\\Desktop\\ServerFiles");
                    if ((Objects.equals(lst, ""))) {
                        JOptionPane.showMessageDialog(null, "Server is empty");
                    }else {
                        stat="LST";
                        c.send(client.getS(), "LST");
                    }
                }
            }
        });
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isStatus()){
                    JOptionPane.showMessageDialog(null, "Connect to server!");
                }else {
                    lst=f.listFiles(  home+"\\Desktop\\ServerFiles");
                    if ((Objects.equals(lst, ""))) {
                        JOptionPane.showMessageDialog(null, "Server is empty");
                    }else {
                        MSG.setEditable(false);
                        popBtn.setText("delete");
                        pop_gui.setVisible(true);
                        pop_gui.setAlwaysOnTop(true);
                        stat="LST";
                        c.send(client.getS(), "LST");
                    }
                }
            }
        });

        con.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!client.isStatus()){
                    lc.connectClient(client);
                    if (client.isStatus()){
                        stat="CON";
                        c.send(client.getS(), "CON");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Already connected to Server");
                }
            }
        });

        dsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.isStatus()){
                    stat="DSC";
                    c.send(client.getS(),"DSC");
                    client.setStatus(false);
                }else{
                    JOptionPane.showMessageDialog(null, "Already disconnected from Server");
                }
            }
        });

    }


}
