package pis.hue2.client;

import pis.hue2.common.Instruction;
import pis.hue2.common.User;
import pis.hue2.common.files;
import pis.hue2.common.msg;
import pis.hue2.server.LaunchServer;

import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class serverHandler {
    msg m;
    files f;
    String path;
    File file;
    Date date =new Date();
    DateFormat df=new SimpleDateFormat("HH:mm:ss");

    /**
     * Thread waits for responses from server and acts accordingly
     * @param client client/Socket receiving the response from the server
     * @param gui Client GUI
     */
    public serverHandler(User client, ClientGUI gui) throws IOException {
        String home =System.getProperty("user.home");
        m =new msg();
        f=new files();

        Thread t=new Thread(new Runnable() {
            String MSG;
            @Override
            public void run() {
                   do {
                        MSG = m.receive(client.getS());

                        if (MSG != null) {
                           if (MSG.equals(Instruction.ACK.toString())) {
                               if (Objects.equals(gui.stat, "CON")) {
                                   m.send(client.getS(),df.format(date)+" : "+ client.getName() + "  has connected successfully!");
                                   SwingUtilities.invokeLater(() -> {
                                       gui.showMSG("Connected to Server successfully!");
                                       gui.stat="";
                                   });
                                   }
                                else if (Objects.equals(gui.stat, "LST")) {
                                   MSG = m.receive(client.getS());
                                   if (gui.pop_gui.isVisible()){
                                       SwingUtilities.invokeLater(() -> {
                                           gui.pop_showMSG(MSG);
                                           gui.stat="";
                                       });
                                   }else {
                                       SwingUtilities.invokeLater(() -> {
                                           gui.showMSG(MSG);
                                           gui.stat="";
                                       });
                                       m.send(client.getS(), df.format(date) + " : " + client.getName() + " used list.");
                                   }

                               } else if (Objects.equals(gui.stat, "PUT")) {

                                   m.send(client.getS(), df.format(date)+" : "+client.getName()+" has requested file upload.");

                                   file=ClientGUI.file;
                                   f.openFile(file);
                                   m.send(client.getS(), Instruction.DAT + " " +file.length());

                                   MSG=m.receive(client.getS());
                                   if (MSG.equals(Instruction.ACK.toString())){
                                       f.sendFile(client.getS(), file);
                                       SwingUtilities.invokeLater(() -> {
                                           gui.showMSG("Server-> File received Successfully!");
                                           gui.stat="";
                                       });

                                       m.send(client.getS(),df.format(date)+" : "+client.getName()+" uploaded the file successfully.");
                                   }

                               } else if (Objects.equals(gui.stat, "GET")) {

                                   m.send(client.getS(),Instruction.ACK+" "+ df.format(date)+" : "+client.getName()+" has requested file download.");

                                   MSG = m.receive(client.getS());

                                   if (MSG.split(" ")[0].equals(Instruction.DAT.toString())) {
                                       path = home+"\\Downloads\\";
                                       f.receiveFile(client.getS(), path, Long.parseLong(MSG.split(" ")[1]));
                                       m.send(client.getS(), Instruction.ACK +" " + df.format(date)+" : "+client.getName()+" has downloaded the file successfully." );
                                       MSG = m.receive(client.getS());
                                       SwingUtilities.invokeLater(() -> {
                                           gui.showMSG(MSG);
                                           gui.stat="";
                                       });
                                   }


                               } else if (Objects.equals(gui.stat, "DEL")) {
                                  MSG= m.receive(client.getS());
                                  if (MSG.split(" ")[0].equals(Instruction.ACK.toString())){
                                      m.send(client.getS(),df.format(date)+" : "+client.getName()+" has deleted the file "+MSG.split(" ")[1]+ " successfully.");
                                      SwingUtilities.invokeLater(() -> {
                                          gui.showMSG(MSG.split(" ")[1]+" has been deleted successfully.");
                                          gui.stat="";
                                      });
                                  }

                               } else if (Objects.equals(gui.stat, "DSC")) {
                                   m.send(client.getS(),df.format(date)+" : "+ client.getName() + " has disconnected.");
                                   SwingUtilities.invokeLater(() -> {
                                       gui.stat="";
                                   });
                                   try {
                                       LaunchServer.socketList.remove(client.getS());
                                       client.getS().close();
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }

                               }
                           }else if(MSG.split(" ")[0].equals(Instruction.DSC.toString())){
                               String fMSG = MSG.replaceFirst("DSC ","");
                               SwingUtilities.invokeLater(() -> {
                                   gui.showMSG(fMSG);
                                   gui.stat="";
                               });
                               m.send(client.getS(),df.format(date)+" : "+ client.getName() + " has disconnected.");
                               try {
                                   LaunchServer.socketList.remove(client.getS());
                                   client.getS().close();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else{
                                System.out.println("New Message: " + MSG);
                                SwingUtilities.invokeLater(() -> {
                                    gui.showMSG(MSG);
                                });
                            }
                        }else{
                            client.setStatus(false);
                        }
                    } while (client.isStatus());
                    try {
                        client.getS().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        });t.start();
    }



}
