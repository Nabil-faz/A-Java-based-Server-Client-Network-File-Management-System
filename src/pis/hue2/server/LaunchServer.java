package pis.hue2.server;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @author Omar Al-Hadha
 * @author Nabil Al-Hadha
 * @version 1.000009
 */
public class LaunchServer {

    public static ServerSocket ss; //Server
    static int port=4999; //default port
    public static boolean status;
    public static ArrayList<Socket> socketList=new ArrayList();//list with all Sockets
    int connections=0; //Socket connections number

    static ServerGUI g=new ServerGUI();

    /**
     * Main methode which starts the server
     */
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(()->{
            g.createGUI();
        });

    }

    /**
     * The Server is started and a socket waits for a connection from a client.
     * The connected client is run in a clientHandler thread while the thread here waits for another socket(Client) to get connected
     */

    public  void startServer() {
        String home=System.getProperty("user.home");
        File newF=new File(home+"\\Desktop\\ServerFiles");
        if (!newF.exists()){
            newF.mkdirs();
            SwingUtilities.invokeLater(() -> {
                g.showMSG("New folder has been created at: "+newF.getAbsolutePath());
            });
        }
        try {
            ss = new ServerSocket(port);
            System.out.println("Server is listening to port " + port);
        } catch (BindException e) {
            JOptionPane.showMessageDialog(null, "Server is already running");
        } catch (IOException e) {
            System.out.println("Connection could not be created");
            e.printStackTrace();
        }
        status = true;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!ss.isClosed()) {
                    try {
                        socketList.add(ss.accept());
                        clientHandler ch = new clientHandler(socketList.get(connections), g);
                        ch.start();
                        System.out.println("Client accepted");
                        connections++;
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(null, "Server is currently closed!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    /**
     * Checks if the Server is running or not
     * if its still running then it Stops the server and clears the socketList
     */

    public void stopServer() {
        status=false;
        if (ss==null||ss.isClosed()){
            JOptionPane.showMessageDialog(null, "Server is currently closed!");
        }else {
            try{
                ss.setSoTimeout(150);
                ss.close();
                SwingUtilities.invokeLater(()->{
                    g.showMSG("Server is closed");
                });
                System.out.println("Server is closed");
                connections=0;
                socketList.clear();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
