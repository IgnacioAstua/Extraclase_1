import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class chat {
    public static void main(String[] args) {
        ventana ventana1 = new ventana();
        ventana1.setVisible(true);

    }
}
class ventana2 extends JFrame{
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
    public panel(){
        add(texto1);
        add(txt);
        add(enviar);
        enviar.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        System.out.print("Enviar");
    }
}