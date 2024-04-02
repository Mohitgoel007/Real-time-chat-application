import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // declare components
    private JLabel Heading = new JLabel("client area");
    private JTextArea message_area = new JTextArea();
    private JTextField message_input = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    // constructors
    public Client(){
        try{
            System.out.println("Sending request to server..!");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connectiobn done..!");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
//
            createGUI();
            handleeevnts();
            startreading();
//            startwriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleeevnts() {

        message_input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("key released"+e.getKeyCode());
                if (e.getKeyCode()==10){
                    System.out.println("you've pressed enter button");
                    String contenttosend = message_input.getText();
                    message_area.append("Me : "+contenttosend+"\n");
                    out.println(contenttosend);
                    out.flush();
                    message_input.setText("");
                    message_input.requestFocus();
                }
            }
        });
    }

    // creating gui
    private void createGUI(){
        // gui code
        this.setTitle("client messenger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // code for component
        Heading.setFont(font);
        message_area.setFont(font);
        message_input.setFont(font);
        Heading.setIcon(new ImageIcon("live-chat-icon-7416.png"));
        Heading.setHorizontalTextPosition(SwingConstants.CENTER);
        Heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        Heading.setHorizontalAlignment(SwingConstants.CENTER);
        Heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        message_area.setEditable(false);
        message_area.setAutoscrolls(false);

        message_input.setHorizontalAlignment(SwingConstants.CENTER);

        // code for setting frame layout
        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(Heading,BorderLayout.NORTH);
        JScrollPane jsp = new JScrollPane(message_area);
        this.add(jsp,BorderLayout.CENTER);
        this.add(message_input,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    

// Start  reading [method]
    public void startreading(){
        //thread - read krke deta rahega
        Runnable r1 = ()->{
            System.out.println("Reader stared...!");
            try {
                while (!socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat..!");
                        JOptionPane.showMessageDialog(this,"server terminated the chat");
                        message_input.setEnabled(false);
                        socket.close();
                        break;
                    }
//                    System.out.println("server : " + msg);
                    message_area.append("server : " + msg +"\n");
                    message_area.setCaretPosition(message_area.getDocument().getLength());
                }

                System.out.println("Connection is closd...!");
            }
                catch (Exception e){
//                    e.printStackTrace();
//                    System.out.println("Connection is closd...!");
            }
        };
        new Thread(r1).start();
    }
// start writing method to send
    public void startwriting(){
        //thread - data user lega and the send krega client tak
        Runnable r2 = ()->{
            System.out.println("Writer stared...!");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
                System.out.println("Connection is closd...!");
            }
                catch (Exception e ){
//                    e.printStackTrace();
//                    System.out.println("Connection is closd...!");
                }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is Client... going to start");
        new Client();
    }
}
