package pis.hue2.server;

import pis.hue2.common.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class clientHandler extends Thread {
    Socket client;
    String path,data;
    File file;
    String message;

    ServerGUI gui;
    msg m;
    files f;

    /**
     * Class constructor
     * @param client connected socket
     * @param gui used GUI
     */
    public clientHandler(Socket client, ServerGUI gui) {
        this.client=client;
        this.gui=gui;
    }

    /**
     * The thread waits for requests/messages from client and responses accordingly
     * Requests types:
     * 1.CON -> The Socket is trying to connect to the serversocket
     * 2.LST -> List the files available on the server
     * 3.PUT -> Client is requesting to upload file to server
     * 4.GET -> Client is requesting to download a file from the server
     * 5.DEL -> Client is requesting to delete a file on the server
     * 6.DSC -> Client is requesting to disconnect from the server
     *
     * Response types:
     * 1.ACK -> Request was successful
     * 2.DND -> Request was denied
     * 3.DAT -> Data to send
     */
    @Override
    public void run() {
        String home=System.getProperty("user.home");
        m =new msg();
        f=new files();

        while (!LaunchServer.ss.isClosed()) {
            message = m.receive(client);
            if (message ==null){
                try{
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               break;
            }
            else if (message.equals(Instruction.CON.toString())) {
                m.send(client, Instruction.ACK.toString());

            } else if (message.equals(Instruction.LST.toString())) {
                m.send(client, Instruction.ACK.toString());
                path = home+"\\Desktop\\ServerFiles";
                m.send(client, f.listFiles(path));

            } else if (message.split(" ")[0].equals(Instruction.PUT.toString())) {
                m.send(client, Instruction.ACK.toString());
                data= message.split(" ")[1];

                message =m.receive(client);
                SwingUtilities.invokeLater(() -> {
                    gui.showMSG(message);
                });

                path = home+"\\Desktop\\ServerFiles\\";
                message = m.receive(client);
                if (message.split(" ")[0].equals(Instruction.DAT.toString())) {
                    m.send(client,Instruction.ACK.toString());
                    f.receiveFile(client, path, Long.parseLong(message.split(" ")[1]));
                    message =m.receive(client);
                    SwingUtilities.invokeLater(() -> {
                        gui.showMSG(message);
                    });
                }

            } else if (message.split(" ")[0].equals(Instruction.GET.toString())) {
                m.send(client, Instruction.ACK.toString());
                data= message.split(" ")[1];

                message =m.receive(client);
                String fMSG = message.replaceFirst("ACK ","");
                SwingUtilities.invokeLater(() -> {
                    gui.showMSG(fMSG);
                });
                    if (message.split(" ")[0].equals(Instruction.ACK.toString())){
                        if (Objects.equals(data, "") ||data==null){
                            m.send(client, Instruction.DND.toString());
                        }else {
                            path = home+"\\Desktop\\ServerFiles\\";
                            file = new File(path + data);
                            m.send(client, Instruction.DAT +" "+file.length());

                            f.sendFile(client, file);
                            message = m.receive(client);
                                if (message.split(" ")[0].equals(Instruction.ACK.toString())) {
                                    String vMSG = message.replaceFirst("ACK ","");
                                    SwingUtilities.invokeLater(() -> {
                                        gui.showMSG(vMSG);
                                    });
                                    m.send(client, data + " has been successfully sent!");
                                }
                        }
                }
            } else if (message.split(" ")[0].equals(Instruction.DEL.toString())) {
                m.send(client, Instruction.ACK.toString());

                path = home+"\\Desktop\\ServerFiles\\";
                data = message.split(" ")[1];
                file = new File(path + data);
                file.delete();
                m.send(client, Instruction.ACK +" "+data);

            } else if (message.equals(Instruction.DSC.toString())) {
                m.send(client, Instruction.ACK.toString());
                message = m.receive(client);
                SwingUtilities.invokeLater(() -> {
                    gui.showMSG(message);
                });
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            } else {
                System.out.println("New Message: " + message);
                SwingUtilities.invokeLater(() -> {
                    gui.showMSG(message);
                });
            }
                }
        try{
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
