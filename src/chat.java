import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class chat {
    public static void main(String[] args) {
        Ventana ventana1 = new Ventana();
        ventana1.setVisible(true);
    }
}

class Ventana extends JFrame {
    public Ventana() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(150, 50);
        setTitle("Chat");
        Panel panel1 = new Panel();
        add(panel1);
    }
}

class Panel extends JPanel implements ActionListener {
    JLabel texto1 = new JLabel("Mensaje");
    JTextField txt = new JTextField(35);
    JButton enviar = new JButton("Enviar");

    ServerSocket serverSocket; // El socket del servidor
    Socket socket;  // El socket para la comunicación con el cliente
    BufferedReader reader;  // Para leer datos del cliente
    PrintWriter writer;  // Para escribir datos al cliente

    public Panel() {
        add(texto1);
        add(txt);
        add(enviar);
        enviar.addActionListener(this);

        // Inicializa el servidor 
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(0); // Elige un puerto disponible
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

    @Override
    public void finalize() {
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Cierra el socket del servidor al finalizar
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
