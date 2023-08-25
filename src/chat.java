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
class Ventana extends JFrame{
    public Ventana() {
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
    JTextField ip = new JTextField(8);
    JTextField port = new JTextField(8);
    JTextArea txt_area = new JTextArea(20, 35);
    public panel(){
        JLabel Ip = new JLabel("IP:");
        add(Ip);
        add(ip);
        JLabel puerto = new JLabel("Puerto:");
        add(puerto);
        add(port);
        JButton conectar = new JButton("Conectar");
        Conexion conexion = new Conexion();
        conectar.addActionListener(conexion);
        add(conectar);
        JLabel sms = new JLabel("Mensajes:");
        add(sms);
        txt_area.setEditable(false);
        add(txt_area);
        add(txt);
        JButton enviar = new JButton("Enviar");
        Envia envio = new Envia();
        enviar.addActionListener(envio);
        add(enviar);
        Thread hilo1 = new Thread(this);
        hilo1.start();

    }
    private class Conexion implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("GG");
            try {
                Socket socket_conexion = new Socket("192.168.0.20", 9990);
                Cliente datos = new Cliente();
                datos.setIp(ip.getText());
                datos.setPort(port.getText());
                ObjectOutputStream usuario = new ObjectOutputStream(socket_conexion.getOutputStream());
                usuario.writeObject(datos);
                socket_conexion.close();
            }
            catch (UnknownHostException e1){
                e1.printStackTrace();
            }
            catch (IOException e1){
                System.out.println(e1.getMessage());
            }
        }
    }

    private class Envia implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String IP = ip.getText();
            try {
                Socket misocket = new Socket(IP, 9990);
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
        int puerto = Integer.parseInt(port.getText());
        try {
            ServerSocket servidor = new ServerSocket(puerto);
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
class Cliente implements Serializable{
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