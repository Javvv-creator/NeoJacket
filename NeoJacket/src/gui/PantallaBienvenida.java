package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class PantallaBienvenida extends JFrame {

    public PantallaBienvenida() {

        // Quita la barra de título y los bordes nativos de la ventana
        setUndecorated(true);

        // Obtiene el tamaño de la pantalla
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(pantalla.width, pantalla.height);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fondo negro de seguridad
        getContentPane().setBackground(Color.BLACK);

        // Obtener la URL del GIF
        URL gifUrl = getClass().getResource("/gui/image/bienvenida.gif");

        // --- SOLUCIÓN CON TAMAÑO EN PÍXELES MANUALES/DINÁMICOS ---
        // Aquí le pasamos exactamente los píxeles de ancho (pantalla.width) 
        // y alto (pantalla.height) del monitor actual para que no quede ningún borde.
        String htmlImagen = "<html><img src='" + gifUrl + "' width='" + pantalla.width + "' height='" + pantalla.height + "'></html>";
        
        JLabel fondo = new JLabel(htmlImagen);

        // Añadimos el JLabel al centro para que cubra la interfaz
        add(fondo, BorderLayout.CENTER);

        // --- Temporizador y transición (12 segundos) ---
        Timer timer = new Timer(12000, e -> {

            InicioNeo inicio = new InicioNeo();
            inicio.setVisible(true);

            dispose(); // Cierra la pantalla de bienvenida

        });

        timer.setRepeats(false);
        timer.start();

        // Hace visible la ventana
        setVisible(true);
    }
}