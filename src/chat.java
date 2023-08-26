import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
//192.168.0.20

public class chat {
    public static void main(String[] args) {
        Ventana ventana1 = new Ventana();
        ventana1.setVisible(true);

    }
}

// Ventana del chat
class Ventana extends JFrame {
    public Ventana() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(150, 50);
        setTitle("Chat");
        panel panel1 = new panel();
        add(panel1);
    }
}

// Panel que contiene los elementos de la interfaz
class panel extends JPanel implements Runnable {
    JTextField txt = new JTextField(30); // Campo de texto para escribir el mensaje
    JTextField nombre = new JTextField(30); // Campo de texto para escribir el nombre
    JTextField ip = new JTextField(10);// Campo de texto para escribir la IP a la cual se enviara el mensaje
    JTextField port = new JTextField(8);// Campo de texto para escribir el puerto que se usara
    JTextArea txt_area = new JTextArea(20, 35);// Area de texto que recibe los mensajes
    String port_s = port.getText();
    int x = 10;
    int y;

    public panel() {
        JLabel Ip = new JLabel("IP:");
        add(Ip);
        add(ip);
        JLabel puerto = new JLabel("Puerto:");
        add(puerto);
        port.setText("1000");
        add(port);
        JButton conectar = new JButton("Conectar");
        Conexion conexion = new Conexion();
        conectar.addActionListener(conexion);
        add(conectar);
        JLabel sms = new JLabel("Chat:");
        add(sms);
        txt_area.setEditable(false);
        add(txt_area);
        JLabel nombre_txt = new JLabel("Nombre");
        add(nombre_txt);
        add(nombre);
        JLabel mensaje_txt = new JLabel("Mensaje:");
        add(mensaje_txt);
        add(txt);
        JButton enviar = new JButton("Enviar");
        Envia envio = new Envia();
        enviar.addActionListener(envio);
        add(enviar);
        Thread hilo1 = new Thread(this);
        hilo1.start();

        y = 1000;
    }

    private class Conexion implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            port_s = port.getText();
            y = Integer.parseInt(port_s);
            System.out.print(y);
            try {
                Socket socket_conexion = new Socket("192.168.0.20", 9990);
                Cliente datos = new Cliente();
                datos.setIp(ip.getText());
                datos.setPort(port.getText());
                ObjectOutputStream usuario = new ObjectOutputStream(socket_conexion.getOutputStream());
                usuario.writeObject(datos);
                socket_conexion.close();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    private class Envia implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String Nombre = nombre.getText() + ": ";
            System.out.print(y);
            try {
                Socket misocket = new Socket("192.168.0.20", 9990);
                DataOutputStream salida = new DataOutputStream(misocket.getOutputStream());
                salida.writeUTF(Nombre + txt.getText());
                salida.close();
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
            ServerSocket receptor = new ServerSocket(y);
            while (true) {
                Socket misocket = receptor.accept();
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

class Cliente implements Serializable {
    private String ip, port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}