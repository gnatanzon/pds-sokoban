package vista;

import controlador.ControladorJuego;
import tablero.CargadorNivel;
import tablero.Tablero;
import tablero.constructor.FabricaElementosSokoban;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PanelTablero panelTablero;
    private final String rutaNivel;

    public VentanaPrincipal(Tablero tablero, String rutaNivel) {
        this.rutaNivel = rutaNivel;
        CargadorAssets assets = new CargadorAssets();
        this.panelTablero = new PanelTablero(tablero, assets);

        ControladorJuego controlador = new ControladorJuego(
                tablero,
                panelTablero::repaint,
                this::mostrarNivelSuperado
        );
        addKeyListener(controlador);

        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        add(panelTablero, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        panel.setBackground(new Color(40, 40, 40));

        JButton btnReiniciar = new JButton("🔄 Reiniciar nivel");
        btnReiniciar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnReiniciar.setFocusable(false); // que no robe el foco del teclado
        btnReiniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReiniciar.addActionListener(e -> reiniciarNivel());

        panel.add(btnReiniciar);
        return panel;
    }

    private void mostrarNivelSuperado() {
        panelTablero.repaint();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("¡Nivel Superado!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(34, 139, 34));

        JLabel subtitulo = new JLabel("¡Todas las cajas llegaron al destino!", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(subtitulo, BorderLayout.CENTER);

        String[] opciones = {"Repetir nivel", "❌ Salir"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                panel,
                "Nivel completado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarNivel();
        } else {
            System.exit(0);
        }
    }

    private void reiniciarNivel() {
        try {
            FabricaElementosSokoban fabrica = new FabricaElementosSokoban();
            CargadorNivel cargador = new CargadorNivel(fabrica);
            Tablero nuevoTablero = cargador.cargar(rutaNivel);

            getContentPane().removeAll();
            removeKeyListeners();

            CargadorAssets assets = new CargadorAssets();
            this.panelTablero = new PanelTablero(nuevoTablero, assets);

            ControladorJuego nuevoControlador = new ControladorJuego(
                    nuevoTablero,
                    panelTablero::repaint,
                    this::mostrarNivelSuperado
            );
            addKeyListener(nuevoControlador);

            add(panelTablero, BorderLayout.CENTER);
            add(crearPanelBotones(), BorderLayout.SOUTH);

            pack();
            repaint();
            requestFocus();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al reiniciar el nivel: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeKeyListeners() {
        for (java.awt.event.KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }
    }

    public PanelTablero obtenerPanelTablero() {
        return panelTablero;
    }
}
