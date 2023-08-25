import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class server {
    private static final int PORT = 0; // Elige un puerto disponible automáticamente

    public static void main(String[] args) {
        try {
            // Crea un socket del servidor en el puerto especificado
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de chat iniciado en el puerto " + serverSocket.getLocalPort());

            // Bucle principal para aceptar conexiones entrantes
            while (true) {
                // Espera y acepta una conexión entrante de un cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Crea un manejador de chat para el cliente y lo ejecuta en un hilo del pool
                ChatHandler chatHandler = new ChatHandler(clientSocket);
                ExecutorService executor = Executors.newFixedThreadPool(10);
                executor.execute(chatHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ChatHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatHandler(Socket socket) throws IOException {
        // Inicializa flujos de entrada y salida para la comunicación con el cliente
        this.clientSocket = socket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String message;
            // Bucle para leer mensajes del cliente
            while ((message = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);
                // Falta aqui implementar la lógica para retransmitir el mensaje a otros clientes conectados
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Cierra los flujos y el socket del cliente
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


