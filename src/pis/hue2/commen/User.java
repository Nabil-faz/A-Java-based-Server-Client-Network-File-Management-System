package pis.hue2.common;

import java.net.Socket;

public class User {
    private String name;
    private Socket s;
    private String ip;
    private int port;
    private boolean status=false;

    /**
     * class Constructor
     * @param name User name
     * @param s user socket
     * @param ip user ip-adresse
     * @param port user port
     */
    public User(String name, Socket s, String ip, int port) {
        this.name = name;
        this.s = s;
        this.ip = ip;
        this.port=port;
    }

    public boolean isStatus() {return status;}

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
