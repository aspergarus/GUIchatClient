package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mustafa1453
 */
public class View {
    private final JFrame main, connectDialog;
    private final JTextField serverIPtext, messageInput, userName;
    private final JTextArea messagesField;
    private final JList peopleField;
    public JButton connectBtn, sendButton;
    public JMenuItem connectMenuItem, exitMenuItem, disconnectMenuItem;

    public View() {
        /**
         * Controller to interact with view.
         */
        Controller ctrl = new Controller(this);

        /**
         * Main window and connect dialog to ask host and username.
         */
        main = new JFrame();
        connectDialog = new JFrame();

        /**
         * Configuring connect dialog window.
         */
        serverIPtext = new JTextField("127.0.0.1", 10);
        userName = new JTextField("Anonymous", 15);
        connectBtn = new JButton("Connect");
        FlowLayout fl = new FlowLayout();
        connectDialog.setLayout(fl);
        connectDialog.add(new JLabel("Enter ip of server: "));
        connectDialog.add(serverIPtext);
        connectDialog.add(new JLabel("User name: "));
        connectDialog.add(userName);
        connectDialog.add(connectBtn);
        connectDialog.setResizable(false);
        connectDialog.setSize(300, 115);
        connectDialog.setTitle("Connection");
        connectDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectDialog.setVisible(false);

        /**
         * Create field for chat messages.
         */
        messagesField = new JTextArea("");
        messagesField.setColumns(20);
        messagesField.setRows(20);
        messagesField.setEditable(false);

        /**
         * Create scrolling for chat messages.
         */
        JScrollPane messagesScroll = new JScrollPane(messagesField);
        messagesScroll.setViewportView(messagesField);
        messagesScroll.setMinimumSize(new Dimension(250, 1));

        /**
         * Create left panel which contains text field to send data,
         * button and field of received messages.
         */
        JPanel leftPanel = new JPanel();
        sendButton = new JButton("Send");
        sendButton.addActionListener(ctrl);
        messageInput = new JTextField(32);
        messageInput.setName("messageInput");
        messageInput.addActionListener(ctrl);
        messageInput.addKeyListener(ctrl);
        leftPanel.add(messagesScroll);
        leftPanel.add(messageInput);
        leftPanel.add(sendButton);

        GroupLayout gl = new GroupLayout(leftPanel);
        leftPanel.setLayout(gl);
        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(messagesScroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGroup(gl.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageInput)
                .addGap(10, 10, 10)
                .addComponent(sendButton))
        );
        gl.setVerticalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(gl.createSequentialGroup()
                .addComponent(messagesScroll, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(messageInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton))
                .addContainerGap())
        );

        /**
         * Create list of users.
         */
        peopleField = new JList();
        peopleField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        peopleField.setMaximumSize(new Dimension(300, 1200));
        peopleField.setDragEnabled(true);
        
        /**
         * Create scrolling for list of users.
         */
        JScrollPane peopleScroll = new JScrollPane();
        peopleScroll.setViewportView(peopleField);
        peopleScroll.setMinimumSize(new Dimension(120, 1));
        peopleScroll.setMaximumSize(new Dimension(300, 1200));
        peopleScroll.setWheelScrollingEnabled(true);

        /**
         * Create SplitPane,
         * which contains field of chat messages and list of users.
         */
        JSplitPane infoPane = new JSplitPane();
        infoPane.setLeftComponent(leftPanel);
        infoPane.setRightComponent(peopleScroll);
        infoPane.setDividerLocation(450);
        infoPane.setResizeWeight(1.0);

        // Create and configure menu.
        JMenu menu = new JMenu();
        JMenuBar menuBar = new JMenuBar();
        connectMenuItem = new JMenuItem("Connect");
        disconnectMenuItem = new JMenuItem("Disconnect");
        disconnectMenuItem.setVisible(false);
        exitMenuItem = new JMenuItem("Exit");
        menu.setText("File");
        menu.add(connectMenuItem);
        menu.add(disconnectMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);

        // Configure main window.
        main.setContentPane(infoPane);
        main.setJMenuBar(menuBar);
        main.setResizable(true);
        main.setSize(600, 420);
        main.setTitle("AlexChat");
        // main.setDefaultCloseOperation(JFrame.);
        main.addWindowListener(ctrl);
        main.setMinimumSize(new Dimension(600, 420));
        main.setVisible(true);

        /**
         * Add listener object to view components.
         */
        exitMenuItem.addActionListener(ctrl);
        connectBtn.addActionListener(ctrl);
        disconnectMenuItem.addActionListener(ctrl);
        connectDialog.addWindowListener(ctrl);
        connectMenuItem.addActionListener(ctrl);
    }

    /**
     * Change from main frame to connection frame.
     */
    void showConnectForm() {
        main.setVisible(false);
        connectDialog.setVisible(true);
    }

    /**
     * Change from connection frame to main frame.
     */
    void showMainForm() {
        connectDialog.setVisible(false);
        main.setVisible(true);
    }

    /**
     * Get ip from textfield.
     * @return IP String.
     */
    String getIPfromText() {
        return serverIPtext.getText();
    }

    /**
     * Get username from textfield.
     * @return username string.
     */
    String getUserName() {
        return userName.getText();
    }

    /**
     * Add new message to chat messages field.
     */
    void addNewMsg(String str) {
        messagesField.append(str + "\n");
    }

    /**
     * Get message from user input text field.
     * @return one message string.
     */
    String getMessage() {
        return messageInput.getText();
    }

    /**
     * Clear chat messages field.
     */
    void clearMessage() {
        messageInput.setText("");
    }

    void updateUsers(String users) {
        peopleField.setListData(users.split(","));
    }

    void hideAll() {
        main.setVisible(false);
        connectDialog.setVisible(false);
    }
}
