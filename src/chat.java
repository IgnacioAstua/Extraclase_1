import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

//Joseph
public class chat {
    public static void main(String[] args) {
        ventana ventana1 = new ventana();
        ventana1.setVisible(true);

    }
}
class ventana extends JFrame{
    public ventana() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(150, 50);
        setTitle("Chat");
        panel panel1 = new panel();
        add(panel1);
    }
}
class panel extends JPanel implements ActionListener{
    JLabel texto1 = new JLabel("Mensaje");
    JTextField txt = new JTextField(35);
    JButton enviar = new JButton("Enviar");

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public panel(){
        add(texto1);
        add(txt);
        add(enviar);
        enviar.addActionListener(this);

        // Inicializa el servidor aparte
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(0); // Elige un puerto disponible
                System.out.println("Servidor escuchando en el puerto: " + serverSocket.getLocalPort());

                while (true) {
                    socket = serverSocket.accept();  // Espera a que un cliente se conecte
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();  // Inicia el hilo del servidor
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String message = txt.getText();

            // Envia el mensaje al servidor
            if (writer != null) {
                writer.println(message);
            }

            txt.setText(""); // Limpia el campo de texto
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}