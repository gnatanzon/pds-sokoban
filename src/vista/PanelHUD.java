package vista;

import controlador.GestorNiveles;
import controlador.memento.HistorialMovimientos;
import sonido.GestorSonido;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;


public class PanelHUD extends JPanel {

    private static final Color COLOR_FONDO  = new Color(25, 25, 35);
    private static final Color COLOR_TEXTO  = new Color(220, 220, 220);
    private static final Color COLOR_TITULO = new Color(255, 210, 50);
    private static final Font  FUENTE_NIVEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font  FUENTE_STAT  = new Font("Segoe UI", Font.PLAIN, 14);

    private final JLabel etNivel;
    private final JLabel etMovimientos;
    private final JLabel etEmpujes;
    private final JLabel etUndosDisponibles;
    private final JButton btnDeshacer;
    private final JButton btnReiniciar;
    private final JButton btnMute;
    private final JSlider sliderVolumen;


    public PanelHUD(Runnable accionDeshacer, Runnable accionReiniciar) {
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));

        etNivel              = crearEtiqueta("Nivel: -",         FUENTE_NIVEL, COLOR_TITULO);
        etMovimientos        = crearEtiqueta("Movs: 0",          FUENTE_STAT,  COLOR_TEXTO);
        etEmpujes            = crearEtiqueta("Empujes: 0",       FUENTE_STAT,  COLOR_TEXTO);
        etUndosDisponibles   = crearEtiqueta("Undos: 0",  FUENTE_STAT,  COLOR_TEXTO);

        btnDeshacer  = crearBoton("Undo",  new Color(70, 70, 150), accionDeshacer);
        btnReiniciar = crearBoton("Reiniciar", new Color(70, 110, 70), accionReiniciar);
        btnMute = new JButton();
        btnMute.setPreferredSize(new Dimension(ICONO_ANCHO + 10, 30));
        btnMute.setBackground(new Color(120, 90, 40));
        btnMute.setFocusable(false);
        btnMute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMute.setBorderPainted(false);
        btnMute.setOpaque(true);
        actualizarIconoMute(); // pinta el ícono inicial
        btnMute.addActionListener(e -> {
            GestorSonido.obtenerInstancia().alternarSilencio();
            actualizarIconoMute();
        });

        sliderVolumen = new JSlider(0, 100, 100);
        sliderVolumen.setPreferredSize(new Dimension(80, 30));
        sliderVolumen.setBackground(COLOR_FONDO);
        sliderVolumen.setOpaque(true);
        sliderVolumen.setFocusable(false);   // NUEVO: evita que le robe el foco al juego
        sliderVolumen.addChangeListener(e -> {
            float valor = sliderVolumen.getValue() / 100f;
            GestorSonido.obtenerInstancia().establecerVolumen(valor);
        });

        add(etNivel);
        add(separador());
        add(etMovimientos);
        add(etEmpujes);
        add(etUndosDisponibles);
        add(separador());
        add(btnDeshacer);
        add(btnReiniciar);
        add(btnMute);
        add(sliderVolumen);
    }

    private static final int ICONO_ANCHO = 44;
    private static final int ICONO_ALTO  = 30;

    private void actualizarIconoMute() {
        boolean silenciado = GestorSonido.obtenerInstancia().estaSilenciado();

        BufferedImage icono = new BufferedImage(ICONO_ANCHO, ICONO_ALTO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icono.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));

        int cx = 13, cy = 15;

        // cuerpo del parlante (polígono tipo "home plate")
        Polygon cuerpo = new Polygon();
        cuerpo.addPoint(cx - 10, cy - 3);
        cuerpo.addPoint(cx - 4,  cy - 3);
        cuerpo.addPoint(cx + 4,  cy - 9);
        cuerpo.addPoint(cx + 4,  cy + 9);
        cuerpo.addPoint(cx - 4,  cy + 3);
        cuerpo.addPoint(cx - 10, cy + 3);
        g.fillPolygon(cuerpo);

        if (silenciado) {
            g.drawLine(cx + 8,  cy - 7, cx + 18, cy + 7);
            g.drawLine(cx + 18, cy - 7, cx + 8,  cy + 7);
        } else {
            g.drawArc(cx + 4, cy - 6,  12, 12, -45, 90);
            g.drawArc(cx + 9, cy - 10, 20, 20, -45, 90);
        }

        g.dispose();
        btnMute.setIcon(new ImageIcon(icono));
    }

    private String textoMute() {
        return GestorSonido.obtenerInstancia().estaSilenciado() ? "🔇" : "🔊";
    }

    //actualiza etiquetas HUD con los datos actuales del historial, se llama desp de cada movimiento o undo
    public void actualizar(HistorialMovimientos historial, GestorNiveles gestorNiveles) {
        int indice = gestorNiveles.obtenerIndiceActual() + 1;
        int total  = gestorNiveles.obtenerTotalNiveles();
        etNivel           .setText("Nivel " + indice + " / " + total);
        etMovimientos     .setText("Movs: "          + historial.getTotalMovimientos());
        etEmpujes         .setText("Empujes: "        + historial.getTotalEmpujes());
        etUndosDisponibles.setText("Undos: "   + historial.getTotalUndos());

        //deshabilita undo cuando no se pueda hacer más
        btnDeshacer.setEnabled(historial.puedeDeshacer());
    }

    // ── Helpers de construcción ──────────────────────────────────────────────

    private JLabel crearEtiqueta(String texto, Font fuente, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(color);
        return label;
    }

    private JButton crearBoton(String texto, Color colorFondo, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(110, 30));
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    private JLabel separador() {
        JLabel sep = new JLabel("│");
        sep.setForeground(new Color(80, 80, 80));
        return sep;
    }
}
