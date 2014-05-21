package gui;

import client.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author mustafa1453
 */
public class Controller extends WindowAdapter implements ActionListener, KeyListener {
    View view = null;
    Client client = null;
    boolean isMain = true;
    private static String serverHost;
    private static String userName;
    public static final int PORT = 8283;

    /**
     * Save view reference to local variable.
     * @param view 
     */
    public Controller(View view) {
        this.view = view;
    }

    @Override
    /**
     * Switch to main frame if close connection frame.
     */
    public void windowClosing(WindowEvent e) {
        if (!isMain) {
            view.showMainForm();
        }
        else {
            if (client != null) {
                disconnect();
            }
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        /**
         * Send new message to server.
         */
        if (o == view.sendButton) {
            sendMessage();
        }

        /**
         * Save IP and username. Run socket client.
         */
        if (o == view.connectBtn) {
            isMain = true;
            serverHost = view.getIPfromText();
            userName  = view.getUserName();
            view.showMainForm();

            // Make one thread to get messages from server.
            client = new Client(this, serverHost, userName, PORT);
            if (client.isConnected()) {
                view.connectMenuItem.setVisible(false);
                view.disconnectMenuItem.setVisible(true);
                client.start();
            }
            else {
                client = null;
            }
        }

        /**
         * Close socket connection.
         */
        if (o == view.disconnectMenuItem) {
            view.connectMenuItem.setVisible(true);
            view.disconnectMenuItem.setVisible(false);
            disconnect();
        }

        /**
         * Open connection frame.
         */
        if (o == view.connectMenuItem) {
            view.showConnectForm();
            isMain = false;
        }

        /**
         * Exit from application.
         */
        if (o == view.exitMenuItem) {
            if (client != null) {
                disconnect();
            }
            System.exit(0);
        }
    }

    /**
     * Send message to server.
     */
    private void sendMessage() {
        if (client != null) {
            client.sendMsgToServer(view.getMessage());
        }
        view.clearMessage();
    }

    /**
     * Add message to view.
     * @param str 
     */
    public void addNewMsg(String str) {
        view.addNewMsg(str);
    }

    /**
     * Update list of users in view.
     * @param String.
     */
    public void updateUsers(String users) {
        view.updateUsers(users);
    }
    

    /**
     * Disconnect from server.
     */
    private void disconnect() {
        client.setStop();
        client.sendMsgToServer("exit");
        client.close();
        client = null;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            sendMessage();
        }
    }
}
