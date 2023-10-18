package guiapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sound.sampled.ReverbType;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.JScrollBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.time.*;
import java.time.format.*;


public class View implements ActionListener, KeyListener{
    private JFrame frame;
    private JFrame logFrame;
    private JPanel topPanel = new JPanel();
    private JPanel logPanel = new JPanel();
    private JPanel topProfilePanel = new JPanel();
    private JPanel chatWindow = new JPanel();
    private JPanel messagePanel = new JPanel();
    private JButton logButton = new JButton("Log in");
    private JLabel usernameLabel = new JLabel("Username");
    private JLabel user = new JLabel("");
    private JLabel receiver = new JLabel("");
    private JTextField usernameText = new JTextField();
    private JButton sendButton = new JButton("Send");
    private JButton userListButton = new JButton("Users");
    private JTextArea userArea = new JTextArea();
    private JTextField messageArea = new JTextField();
    private JTextPane chatArea= new JTextPane();
    private DefaultCaret caret = new DefaultCaret();
    private static final Logger logger = LogManager.getLogger(Main.class);

    String receiverString = "";
    String username = "empty"; 
    String clientIds[] = {"User1"};
    JList userList = new JList(clientIds);

    boolean clientChoose = true;
    boolean refreshExecuted = false;
    String tempMessage = "";
    String ServerMessage = "";
    public void LogFrame(){
          //Log Frame set up

          logFrame = new JFrame("Chat App");
          logFrame.setSize(300, 500);
          logFrame.setBackground(new Color(25,27,35));
          logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          logFrame.setLocationRelativeTo(null);
          logFrame.setResizable(false);
    
          logPanel.setSize(300, 500);
          logPanel.setBackground(new Color(25,27,35));

          usernameLabel.setBounds(100, 100, 100, 50);
          usernameLabel.setHorizontalAlignment(JLabel.CENTER);
          usernameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
          usernameLabel.setForeground(Color.WHITE);
          
          usernameText.setBounds(90, 200, 120, 50);
          usernameText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
          usernameText.setBackground(new Color(12, 14, 18));
          usernameText.setForeground(Color.WHITE);

          logButton.setBounds(100, 300, 100, 50);
          logButton.setBackground(new Color(39,49,73));
          logButton.setForeground(Color.WHITE);
          logButton.addActionListener(this);  
          
          logFrame.getContentPane().setLayout(null);
          logFrame.getContentPane().add(usernameLabel);
          logFrame.getContentPane().add(usernameText);
          logFrame.getContentPane().add(logButton);
          logFrame.getContentPane().add(logPanel);
          logFrame.setVisible(true);
    }
    public void Frame(){


        frame = new JFrame("Chat App");
        frame.setSize(1014, 587);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        topPanel.setBounds(0, 0, 1000, 40);
        topPanel.setBackground(new Color(25,27,35));

        userList.setBounds(350, 45, 300, 400);
        userList.setBorder(BorderFactory.createLoweredBevelBorder());
        userList.setBackground(new Color(14,24,39));
        userList.setForeground(Color.WHITE);
        userList.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        frame.add(userList);
        userList.setVisible(false);
        userList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Check if the selection has finished changing
                    Object selectedValue = userList.getSelectedValue(); // Retrieve the selected value
                    if(selectedValue != null){
                        receiverString = (String) selectedValue;
                        receiver.setText("To: " + receiverString);
                    }
                    userList.setVisible(false);
                    userList.setSelectedIndex(-1);
                    clientChoose = true;
                }
            }
            
        });
        
        chatArea.setEditable(false);
        chatArea.setBounds(250, 45, 750, 430);
        chatArea.setBackground(new Color(12, 14, 18));
        chatArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        chatArea.setForeground(Color.WHITE);
        JScrollPane scrollPane= new JScrollPane(chatArea);
        scrollPane.setBounds(250, 45, 700, 430);
        scrollPane.setBackground(new Color(20,40,80));
        userArea.setEditable(false);
        scrollPane.getVerticalScrollBar().setBackground(new Color(12, 14, 18));
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        verticalScrollBar.setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = (new Color(39,49,73));
            }
            @Override
            protected JButton createDecreaseButton(int orientation){
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation){
                return createZeroButton();
        }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }            
        });

        caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Border bord = new LineBorder(new Color(12, 14, 18), 1);
        chatArea.setBorder(bord);
        scrollPane.setBorder(bord);
        

        Border bordmsgar = new LineBorder(new Color(23, 26, 33), 1);
        messageArea.setBorder(bordmsgar);

        user.setBounds(50, 50, 150, 50);
        user.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        user.setForeground(Color.white);
        user.setText("You: " + username);

        receiver.setBounds(50, 100, 150, 50);
        receiver.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        receiver.setText("To: ");
        receiver.setForeground(Color.white);

        frame.getContentPane().add(user);
        frame.getContentPane().add(receiver);

        messageArea.setEditable(true);
        messageArea.setBounds(130, 495, 740, 50);
        messagePanel.setBounds(0, 490, 870, 60);
        messageArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        messageArea.setBackground(new Color(23, 26, 33));
        messagePanel.setBackground(new Color(23, 26, 33));
        messageArea.setForeground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createLoweredBevelBorder());

        messageArea.addKeyListener(this);
        

        userListButton.setBounds(400,0,200,45);
        userListButton.setBackground(new Color(39,49,73));
        userListButton.setForeground(Color.white);
        userListButton.addActionListener(this);
        


        sendButton.setBounds(870, 485, 130, 65);
        sendButton.setBackground(new Color(39,49,73));
        sendButton.setForeground(Color.white);
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        chatWindow.setBounds(0,30, 1000, 460);


        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(userListButton);
        frame.getContentPane().add(topPanel);
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(messageArea);
        frame.getContentPane().add(sendButton);        
        chatWindow.setBackground(new Color(12, 14, 18));
        frame.getContentPane().add(messagePanel);
        frame.getContentPane().add(chatWindow);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == logButton){
            logger.info("Log Button Pressed");
            username = usernameText.getText();
            logFrame.setVisible(false);

        }
        if (e.getSource() == sendButton){
            sendMessage();
            logger.info("Send Button Pressed");
        }
        if (e.getSource() == userListButton){
            logger.info("User Lits Button Pressed");
            if(userList.isVisible() == true){
                userList.setVisible(false);
            }
            else{
                refreshUsers();
            }
        }
    }

    public void sendMessage(){
        if (receiverString != ""){
            sendButton.setEnabled(true);
        }
  
        tempMessage = messageArea.getText();

        messageArea.setText("");
        
        
    }
    public void printMessage(){
        chatArea.setText("");
        String[] bigParts = ServerMessage.split("\n");
        for (int i = 0; i < bigParts.length; i++){
            String[] parts = bigParts[i].split("\\$\\$");
            if(parts.length>=4){
                ServerMessage = "\n " + parts[4] + "  >" + parts[1] + ": " +  parts[3];
                chatArea.setText(chatArea.getText() + ServerMessage);
            }

        }
    }

    public void refreshUsers(){
        refreshExecuted = true;
        userList.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e){
        char key = e.getKeyChar();
        if (key == '\n' && e.getSource() == messageArea){
            if (receiverString != ""){
                sendButton.setEnabled(true);
                sendMessage();
                logger.info("Send Button Pressed");
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e){

    }
    @Override
    public void keyReleased(KeyEvent e){

    }

    
}


