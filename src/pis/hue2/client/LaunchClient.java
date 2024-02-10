package pis.hue2.client;

import pis.hue2.common.User;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * @author Omar Al-Hadha
 * @author Nabil Al-Hadha
 * @version 1.000009
 */
public class LaunchClient extends ClientGUI {

     ClientGUI gui;
     User client;

    /**
     * Main methode that starts and runs the client
     */
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        ClientGUI gui=new ClientGUI();
        SwingUtilities.invokeAndWait(gui::welcomeWindow);
    }

    /**
     * Class constructor with client data and the GUI assigned
     * @param client client data
     * @param gui assigned GUI
     */
    public LaunchClient(User client,ClientGUI gui){
    this.client=client;
    this.gui=gui;
    }

    /**
     * Connects the Client to the server and runs the client in a new thread(serverHandler)
     * @param client contains the client data to be connected
     */
    public void connectClient(User client){
        try{
            client.setS(new Socket(client.getIp(),client.getPort()));
            SwingWorker<Void,Void> sw=new SwingWorker<Void,Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    serverHandler sh=new serverHandler(client,gui);
                    return null;
                }
            };sw.execute();
            client.setStatus(true);
        }catch (ConnectException e){
            SwingUtilities.invokeLater(() -> {
                gui.showMSG("Server is currently offline");
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
