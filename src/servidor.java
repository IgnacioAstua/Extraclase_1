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

private void publicar_mensaje (final String muestra_mensaje) {
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
                publicar_mensaje("\nSe ha terminado la conexión");
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
    publicar_mensaje("Esperando conexión\n");
    conexion = servidor.accept();
    publicar_mensaje("Conexion " + i + " obtenida de: " + conexion.getInetAddress().getHostAddress());
}