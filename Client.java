package ChatApp;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.io.*;

public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;   
    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Times New Roman", Font.PLAIN, 20);
    //Constructor
    public Client()
    {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter host IP address: ");
            String hostIP = sc.nextLine();
            System.out.println("Enter the port Number: ");
            int portNo = sc.nextInt();
            System.out.println("Sending Request to Server");
            socket = new Socket(hostIP, portNo);
            System.out.println("Connection Done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            //startWriting();
            sc.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Couldn't connect to Server");
        }   
    }
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key Released: " +e.getKeyCode());
                if(e.getKeyCode() == 10)
                {
                    // System.out.println("You have pressed Enter Button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: "+ contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }
        });
    }
    private void createGUI() {
        //gui code
        this.setTitle("Cleint Messenger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //setting the layout of frame
        this.setLayout(new BorderLayout());
        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);        
        this.setVisible(true);
    }
    //start reading method
    public void startReading()
        {
            // thread - read the data

            Runnable r1 = ()->{
                System.out.println("Reader Started..");
                try{
                while(!socket.isClosed())
                {
                    String msg = br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server Terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                        socket.close();
                        messageInput.setEnabled(false);
                        break;
                    }
                    //System.out.println("Server: "+ msg) ;
                    messageArea.append("Server: " + msg+ "\n");
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection Closed");
            }
            System.out.println("Connection Closed");
            };
            new Thread(r1).start();
        }
    public static void main(String[] args) {
        System.out.println("This Is Client");
        new Client();
    }
}
