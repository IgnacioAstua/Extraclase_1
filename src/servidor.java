import java.swing.SwingUtilities;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

private Socket conexion;
private ServerSocket servidor;
private ObjectInputStream entrada;
private ObjectOutputStream salida;
private int i = 1;

private void aviso_mensaje (final String muestra_mensaje) {
    SwingUtilities.invokeLater(
        new Runnable(){
            public void run(){
                pantalla.append(muestra_mensaje);
            }
        } );
}

public void encender_server() {
    try {
        servidor = new ServerSocket(12344, 100);
        while (true) {
            try {
                espera_conexion();
                obtener_flujos();
                procesa_conexion();
            } catch (EOFException e) {
                aviso_mensaje("\nSe ha terminado la conexión");
            } finally {
                cierra_conexion();
                i++;
            }
        }
    } catch (IOException excepcion) {
        excepcion.printStackTrace();
    }
}

private void espera_conexion() throws IOException {
    aviso_mensaje("Esperando conexión\n");
    conexion = servidor.accept();
    aviso_mensaje("Conexion " + i + " obtenida de: " + conexion.getInetAddress().getHostAddress());
}

public void obtener_flujos() throws IOException {
    salida = new ObjectOutputStream(Conexion.getOutputStream());
    salida.flush();
    entrada = new ObjectInputStream(conexion.getInputStream());
    aviso_mensaje("\nObtener flujos: Ok\n");
}

private void enviar_datos(String mensaje) {
    try {
        salida.writeObject("SERVIDOR: " + mensaje);
        salida.flush();
        aviso_mensaje("\nSERVIDOR: " + mensaje);
    } catch (IOException e) {
        pantalla.append("\nError: ");
    }
}