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
private int contador = 1;

private void publicar_mensaje (final String muestra_mensaje) {
    SwingUtilities.invokeLater(
        new Runnable(){
            public void run(){
                pantalla.append(muestra_mensaje);
            }
        } );
}
