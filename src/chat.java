import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;


public class chat {
    public static void main(String[] args) {
        Ventana_ ventana1 = new Ventana_();
        ventana1.setVisible(true);

    }
}
class Ventana_ extends JFrame{
    public Ventana_() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(150, 50);
        setTitle("Chat1");
        panel panel1 = new panel();
        add(panel1);
    }
}
class panel extends JPanel implements Runnable{
    JTextField txt = new JTextField(35);
    JTextField ip = new JTextField(35);
    JTextArea txt_area = new JTextArea(20, 35);
    public panel(){
        JLabel Ip = new JLabel("IP:");
        add(Ip);
        add(ip);
        JLabel sms = new JLabel("Mensajes:");
        add(sms);
        txt_area.setEditable(false);
        add(txt_area);
        add(txt);
        JButton enviar = new JButton("Enviar");
        Envia evento = new Envia();
        enviar.addActionListener(evento);
        add(enviar);
        Thread hilo1 = new Thread(this);
        hilo1.start();

    }


    private class Envia implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String IP = ip.getText();
            try {
                Socket misocket = new Socket(IP, 9999);
                DataOutputStream salida = new DataOutputStream(misocket.getOutputStream());
                salida.writeUTF(txt.getText());
                salida.close();
            }
            catch (UnknownHostException e1){
                e1.printStackTrace();
            }
            catch (IOException e1){
                System.out.println(e1.getMessage());
            }

        }


    }
    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(9999);
            while (true) {
                Socket misocket = servidor.accept();
                DataInputStream entrada = new DataInputStream(misocket.getInputStream());
                String mensaje = entrada.readUTF();
                txt_area.append("\n" + mensaje);
                misocket.close();
            }
        }
        catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}