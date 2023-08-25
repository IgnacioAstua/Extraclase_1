import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class chat {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
        });
    }
}

class Ventana extends JFrame {
    private Panel panel;

    public Ventana() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(150, 50);
        setTitle("Chat");
        panel = new Panel();
        add(panel);
    }
}

class Panel extends JPanel implements Runnable {
    private JTextField txt = new JTextField(35);
    private JTextField ip = new JTextField(35);
    private JTextField userName = new JTextField(15);
    private JTextArea txt_area = new JTextArea(20, 35);
    private JLabel portLabel = new JLabel("Puerto:");

    public Panel() {
        JLabel ipLabel = new JLabel("IP:");
        add(ipLabel);
        add(ip);

        JLabel nameLabel = new JLabel("Nombre:");
        add(nameLabel);
        add(userName);

        JLabel sms = new JLabel("Mensajes:");
        add(sms);
        txt_area.setEditable(false);
        add(txt_area);
        add(txt);

        JButton enviar = new JButton("Enviar");
        Envia evento = new Envia();
        enviar.addActionListener(evento);
        add(enviar);

        add(portLabel);
        int randomPort = (int) (Math.random() * 10000) + 10000;
        portLabel.setText("Puerto: " + randomPort);

        Thread hilo1 = new Thread(this);
        hilo1.start();
    }

    private class Envia implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String IP = ip.getText();
            String mensaje = userName.getText() + ": " + txt.getText();
            try {
                Socket misocket = new Socket(IP, 9999);
                DataOutputStream salida = new DataOutputStream(misocket.getOutputStream());
                salida.writeUTF(mensaje);
                salida.close();
                txt.setText(""); // Elimina el contenido del campo de texto después de enviar el mensaje
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(9999);
            portLabel.setText("Puerto: " + servidor.getLocalPort());
            while (true) {
                Socket misocket = servidor.accept();
                DataInputStream entrada = new DataInputStream(misocket.getInputStream());
                String mensaje = entrada.readUTF();
                txt_area.append("\n" + mensaje);
                misocket.close();
            }
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}









