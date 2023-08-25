import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class chat2 extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String userName; // Nuevo campo para almacenar el nombre del usuario

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JTextField nameField; // Campo para ingresar el nombre

    public chat2(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        // Agrega un campo para ingresar el nombre
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 20));
        inputPanel.add(nameField, BorderLayout.WEST);

        sendButton = new JButton("Enviar");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Iniciar un hilo para recibir mensajes del servidor
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        userName = nameField.getText(); // Obtén el nombre del campo
        out.println(userName + ": " + message); // Agrega el nombre al mensaje
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String serverAddress = "127.0.0.1"; // Cambia a la dirección IP del servidor si es necesario
                int serverPort = 12345; // Cambia al puerto que estés usando en el servidor
                new chat2(serverAddress, serverPort);
            }
        });
    }
}





