package client;

import gui.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


/**
 * Get data from server, send other data to server.
 * 
 * @author Влад
 */
public class Client extends Thread {
    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private final Controller ctrl;
    private static boolean stoped = false;

    /**
     * Initialize streams and socket.
     * 
     * @param ctrl - object link to Controller.
     */
    public Client(Controller ctrl, String ip, String username, int port) {
        this.ctrl = ctrl;

        try {
            socket = new Socket(ip, port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            out.println(username);
        } catch (IOException e) {
            System.err.println("Connection error.");
            ctrl.addNewMsg("Could not connect to server " + ip + ":" + port);
        }
    }

    public boolean isConnected() {
        return socket != null;
    }

    @Override
    /**
     * Get messages from server, if they exists.
     */
    public void run() {
        try {
            while (!stoped) {
                String str = in.readLine();
                if (str.startsWith("users:")) {
                    ctrl.updateUsers(str.substring("users:".length(), str.length()));
                    continue;
                }
                ctrl.addNewMsg(str);
            }
        } catch (IOException e) {
            System.err.println("Error with getting message.");
        }
    }

    /**
     * Stop receiving messages thread.
     */
    public void setStop() {
        stoped = true;
    }

    /**
     * Stop receiving messages thread.
     */
    public boolean getStop() {
        return stoped;
    }

    /**
     * Close streams and socket.
     */
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Socket error");
        }
    }

    /**
     * Send message from controller to server via socket.
     * @param string 
     */
    public void sendMsgToServer(String string) {
        out.println(string);
    }
}