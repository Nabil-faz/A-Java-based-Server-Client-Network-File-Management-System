package pis.hue2.common;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class files {
    /**
     * Object from class msg used mainly for sending or receiving messages
     */
    msg m =new msg();

    /**
     * Opens a chosen file
     * This method is further used to show/open the file sent to the Server
     *
     * @param f : file to open
     */

    public void openFile(File f){
        Desktop des= Desktop.getDesktop();
        try{
            des.open(f);
        } catch (IOException | NullPointerException e ) {
            JOptionPane.showMessageDialog(null,"No file was chosen.");
        }
    }

    /**
     * Choose a file using the JFileChooser and returns the chosen file
     *
     * @param c JFileChooser : used to choose the file
     * @return selected file or null if no file was chosen
     */

    public File chooseFile(JFileChooser c){
        c=new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnVal=c.showOpenDialog(null);
        if (returnVal==JFileChooser.APPROVE_OPTION){
            return c.getSelectedFile();
        }
        return null;
    }

    /**
     * Sends file
     * @param client the Socket sending the file
     * @param file File to be sent
     */

    public synchronized void sendFile(Socket client, File file){
        m.send(client,file.getName());
        try{
            byte b[]=new byte[4*1024];

            FileInputStream fis=new FileInputStream(file);
            DataOutputStream dos=new DataOutputStream(client.getOutputStream());

            int count;

            while((count=fis.read(b))!=-1){
                dos.write(b,0,count);
                dos.flush();
            }
            fis.close();

        }catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null,"No file or wrong file has been selected!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives file
     * @param client Socket receiving the file
     * @param path path to save the received file
     * @param length file size
     */

    public synchronized void receiveFile(Socket client,String path,long length){
        String src= m.receive(client);
        byte b[]=new byte[4*1024];

        try{
            int count;
            FileOutputStream fos=new FileOutputStream(path+src);
            DataInputStream is= new DataInputStream(client.getInputStream());

            while(length>0&&(count=is.read(b,0,(int)Math.min(b.length,length)))!=0){
                fos.write(b,0,count);
                length-=count;
            }

            fos.close();

        }catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"No file or wrong file has been selected!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows files in given folder
     * Further used to list files on Server
     * @param path path to the folder which is going to be listed
     * @return all files names in a String form
     */
    public String listFiles(String path){
        String f= "";
        File file=new File(path);
        File[] files=file.listFiles();

        try {
            for (File c:files) {
                f+=c.getName()+"  ";
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return f;
    }




}
