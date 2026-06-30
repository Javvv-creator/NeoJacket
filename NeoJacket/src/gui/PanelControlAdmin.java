

package gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class PanelControlAdmin extends JFrame {

    private Image fondo;
    private Image logo;

    public PanelControlAdmin() {

        fondo = new ImageIcon(
                getClass().getResource("/gui/image/fondo.png"))
                .getImage();

        logo = new ImageIcon(
                getClass().getResource("/gui/image/logoblanco.png"))
                .getImage();

        setTitle("NeoJacket - Panel de Administración");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new FondoPanel());

        setVisible(true);
    }

    class FondoPanel extends JPanel {

        public FondoPanel() {

            setLayout(null);

            // ======================
            // SIDEBAR
            // ======================

            JPanel sidebar = new JPanel() {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setColor(new Color(25, 38, 35, 220));

                    g2.fillRoundRect(
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            35,
                            35);

                    g2.dispose();
                }
            };

            sidebar.setOpaque(false);
           sidebar.setBounds(20,20,300, 1000);
            sidebar.setLayout(null);

            Image logoEscalado =
        logo.getScaledInstance(
                250,
                110,
                Image.SCALE_SMOOTH);

JLabel lblLogo =
        new JLabel(
                new ImageIcon(
                        logoEscalado));

lblLogo.setBounds(
        20,
        10,
        250,
        110);

            sidebar.add(lblLogo);

            String[] opciones = {
                "Gestión de Usuarios",
                "Gestión de Menores",
                "Gestión de Cuentas",
                "Gestión de Tarjetas",
                "Gestión de Divisas",
                "Gestión de Transacciones"
            };

            int y = 140;

            for (String texto : opciones) {

                JButton btn = new JButton(texto);

                btn.setBounds(
        20,
        y,
        250,
        50);

                btn.setFocusPainted(false);

                btn.setForeground(Color.WHITE);

               btn.setBackground(
                     new Color(25,38,35));

                sidebar.add(btn);
                
                if(texto.equals("Gestión de Usuarios")){

    btn.addActionListener(e -> {

        new GestionUsuario();

        dispose();
    });
}
                if(texto.equals("Gestión de Menores")){
    btn.addActionListener(e -> {
        new GestionMenores();
        dispose();
    });
}
                
                if(texto.equals("Gestión de Cuentas")){
    btn.addActionListener(e -> {
        new GestionCuentas();
        dispose();
    });
}
                
                 if(texto.equals("Gestión de Tarjetas")){
    btn.addActionListener(e -> {
        new GestionTarjeta();
        dispose();
    });

}
                 if(texto.equals("Gestión de Divisas")){
    btn.addActionListener(e -> {
        new GestionDivisas();
        dispose();
    });

}
                 
                  if(texto.equals("Gestión de Transacciones")){
    btn.addActionListener(e -> {
        new GestionTransacciones();
        dispose();
    });

}
                 

                y += 60;
                
                btn.setBorderPainted(false);

btn.addMouseListener(
        new java.awt.event.MouseAdapter() {

    @Override
    public void mouseEntered(
            java.awt.event.MouseEvent e) {

        btn.setBackground(
                new Color(
                        251,
                        232,
                        138));

        btn.setForeground(
                Color.BLACK);
    }

    @Override
    public void mouseExited(
            java.awt.event.MouseEvent e) {

        btn.setBackground(
                new Color(
                        25,
                        38,
                        35));

        btn.setForeground(
                Color.WHITE);
    }
});
            }

            add(sidebar);

            // ======================
            // PANEL BIENVENIDA
            // ======================

            JPanel bienvenida = crearCard(
                    340,
                    40,
                    950,
                    140);

            bienvenida.setLayout(null);

            JLabel titulo = new JLabel("BIENVENIDO");

            titulo.setForeground(new Color(251, 232, 138));
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

            titulo.setBounds(30, 20, 200, 30);

            bienvenida.add(titulo);

            JLabel subtitulo = new JLabel(
                    "Panel de Administración");

            subtitulo.setForeground(Color.WHITE);

            subtitulo.setFont(
                    new Font("Segoe UI",
                            Font.BOLD,
                            32));

            subtitulo.setBounds(30, 50, 450, 40);

            bienvenida.add(subtitulo);

            JLabel desc = new JLabel(
                    "Resumen general de la plataforma");

            desc.setForeground(Color.WHITE);

            desc.setBounds(30, 95, 350, 25);

            bienvenida.add(desc);

            add(bienvenida);

            // ======================
            // TARJETAS
            // ======================

            add(crearTarjeta(
                    "Usuarios",
                    "48",
                    340,
                    220));

            add(crearTarjeta(
                    "Cuentas",
                    "45",
                    620,
                    220));

            add(crearTarjeta(
                    "Tarjetas",
                    "37",
                    900,
                    220));

            add(crearTarjeta(
                    "Menores",
                    "20",
                    1180,
                    220));

            add(crearTarjeta(
                    "Transacciones",
                    "24",
                    1460,
                    220));
            
            
            // ======================
// PANEL ADMINISTRADOR
// ======================

JPanel admin = crearCard(
        1310,
        40,
        380,
        140);

admin.setLayout(null);

JLabel nombre = new JLabel("Administrador");

nombre.setForeground(Color.WHITE);

nombre.setFont(
        new Font(
                "Segoe UI",
                Font.BOLD,
                22));

nombre.setBounds(
        25,
        30,
        250,
        30);

admin.add(nombre);

JLabel rol = new JLabel("Neo Jacket");

rol.setForeground(
        new Color(
                251,
                232,
                138));

rol.setFont(
        new Font(
                "Segoe UI",
                Font.PLAIN,
                18));

rol.setBounds(
        25,
        65,
        200,
        25);

admin.add(rol);

add(admin);

// ======================
// GRAFICA
// ======================

JPanel grafica = crearCard(
        340,
        390,
        950,
        380);

grafica.setLayout(null);

JLabel tituloGrafica =
        new JLabel("Usuarios Registrados");

tituloGrafica.setForeground(Color.WHITE);

tituloGrafica.setFont(
        new Font(
                "Segoe UI",
                Font.BOLD,
                22));

tituloGrafica.setBounds(
        30,
        20,
        300,
        30);

grafica.add(tituloGrafica);

JPanel barras = new JPanel() {

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        int[] datos =
                {80,120,180,240,290};

        int x = 70;

        g2.setColor(
                new Color(
                        251,
                        232,
                        138));

        for(int valor : datos){

            g2.fillRoundRect(
                    x,
                    320 - valor,
                    70,
                    valor,
                    15,
                    15);

            x += 150;
        }
    }
};

barras.setOpaque(false);

barras.setBounds(
        20,
        50,
        900,
        300);

grafica.add(barras);

add(grafica);

// ======================
// ACTIVIDAD RECIENTE
// ======================

JPanel actividad = crearCard(
        1310,
        390,
        380,
        380);

actividad.setLayout(null);

JLabel tituloActividad =
        new JLabel("Actividad Reciente");

tituloActividad.setForeground(
        Color.WHITE);

tituloActividad.setFont(
        new Font(
                "Segoe UI",
                Font.BOLD,
                22));

tituloActividad.setBounds(
        20,
        20,
        250,
        30);

actividad.add(
        tituloActividad);

String[] eventos = {

    "Usuario registrado",
    "Cuenta creada",
    "Tarjeta emitida",
    "Divisa actualizada",
    "Menor agregado",
    "Transferencia realizada",
    "Holi:p"
};

int yy = 80;

for(String e : eventos){

    JLabel lbl =
            new JLabel("• " + e);

    lbl.setForeground(
            Color.WHITE);

    lbl.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    16));

    lbl.setBounds(
            30,
            yy,
            300,
            25);

    actividad.add(lbl);

    yy += 45;
}

add(actividad);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            g.drawImage(
                    fondo,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this);
        }
    }

    private JPanel crearCard(
            int x,
            int y,
            int w,
            int h) {

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(
                        new Color(
                                25,
                                38,
                                35,
                                210));

                g2.fill(new RoundRectangle2D.Double(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        30,
                        30));

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBounds(x, y, w, h);

        return panel;
    }

    private JPanel crearTarjeta(
            String titulo,
            String valor,
            int x,
            int y) {

        JPanel card = crearCard(
                x,
                y,
                240,
                130);

        card.setLayout(null);

        JLabel lblTitulo =
                new JLabel(titulo);

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16));

        lblTitulo.setBounds(
                20,
                15,
                150,
                25);

        card.add(lblTitulo);

        JLabel lblValor =
                new JLabel(valor);

        lblValor.setForeground(
                new Color(
                        251,
                        232,
                        138));

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        40));

        lblValor.setBounds(
                20,
                45,
                150,
                50);

        card.add(lblValor);

        return card;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new PanelControlAdmin();
        });
    }
}

    
