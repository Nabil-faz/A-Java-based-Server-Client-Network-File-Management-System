package pis.hue2.common;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class msg {
    PrintWriter printWriter;
    InputStreamReader streamReader;
    BufferedReader bufferedReader;

    /**
     * Sends a message / Protocol
     * @param s socket sending the message
     * @param msg String : Message to be sent
     */

    public synchronized void send(Socket s,String msg){
        printWriter=null;
        try{
            printWriter=new PrintWriter(s.getOutputStream());
        }catch (NullPointerException e) {
            return;
        }catch (IOException e){
            e.printStackTrace();
        }
        printWriter.println(msg);
        printWriter.flush();
    }

    /**
     * Waits for and returns the received message / Protocol
     * @param s socket receiving the text
     * @return the received string
     */
    public synchronized String receive(Socket s){
        streamReader=null;
        String msg = null;
        try{
            streamReader= new InputStreamReader(s.getInputStream());
            bufferedReader=new BufferedReader(streamReader);
            msg=bufferedReader.readLine();
        }
        catch (SocketException i){
            JOptionPane.showMessageDialog(null,"Connection has been lost");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return msg;
    }
}
