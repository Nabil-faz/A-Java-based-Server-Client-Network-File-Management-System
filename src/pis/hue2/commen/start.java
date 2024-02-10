package pis.hue2.common;

/**
 * @author Omar Al-Hadha
 * @author Nabil Al-Hadha
 * @version 1.000009
 */

import pis.hue2.client.ClientGUI;
import pis.hue2.server.ServerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is used inorder to have a jar file containing both the server and the client
 */
public class start {

    /**
     *Used to start the main application
     */
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        start s=new start();
        s.startGUI();
    }

    /**
     * Initialize the contents of the frame.
     */

    private void startGUI(){
        ServerGUI g=new ServerGUI();

        JFrame frame = new JFrame("Starter");
        frame.setBounds(100, 100, 343, 246);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("");
        label.setBounds(137, 56, 46, 14);
        frame.getContentPane().add(label);

        JButton btnServer = new JButton("Server");
        btnServer.setBounds(34, 147, 89, 37);
        frame.getContentPane().add(btnServer);

        JButton btnClient = new JButton("Client");
        btnClient.setBounds(212, 147, 89, 37);
        frame.getContentPane().add(btnClient);

        JLabel lblClickButtonsBelow = new JLabel("Click buttons below to start Server or Client");
        lblClickButtonsBelow.setBounds(61, 41, 224, 74);
        frame.getContentPane().add(lblClickButtonsBelow);

        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        btnServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                if (ServerGUI.gui==null||!ServerGUI.gui.isVisible()) {
                    g.createGUI();
                }else{
                    JOptionPane.showMessageDialog(null, "Server window is already opened!");
                }
            }
        });

        btnClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientGUI gui=new ClientGUI();
                gui.welcomeWindow();
            }
        });
    }

}
