package vista;

import controlador.GestorNiveles;
import controlador.memento.HistorialMovimientos;

import javax.swing.*;
import java.awt.*;

/**
 * Panel del HUD (Heads-Up Display) que muestra las estadísticas del nivel actual
 * y los botones de acción al jugador.
 *
 * Muestra: nivel actual, movimientos, empujes, undos disponibles.
 * Botones: Deshacer (undo) y Reiniciar nivel.
 *
 * GRASP – Pure Fabrication: clase de soporte que no existe en el dominio del juego.
 *
 * GRASP – High Cohesion: responsabilidad única de presentar el HUD.
 *
 * GRASP – Low Coupling: recibe callbacks (Runnable) en vez de referencias
 * directas al controlador, evitando acoplamiento con ControladorJuego.
 */
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

    /**
     * @param accionDeshacer  se ejecuta al pulsar el botón "Deshacer"
     * @param accionReiniciar se ejecuta al pulsar el botón "Reiniciar"
     */
    public PanelHUD(Runnable accionDeshacer, Runnable accionReiniciar) {
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setLayout(new FlowLayout(FlowLayout.CENTER, 24, 4));

        etNivel              = crearEtiqueta("Nivel: -",         FUENTE_NIVEL, COLOR_TITULO);
        etMovimientos        = crearEtiqueta("Movs: 0",          FUENTE_STAT,  COLOR_TEXTO);
        etEmpujes            = crearEtiqueta("Empujes: 0",       FUENTE_STAT,  COLOR_TEXTO);
        etUndosDisponibles   = crearEtiqueta("Undos usados: 0",  FUENTE_STAT,  COLOR_TEXTO);

        btnDeshacer  = crearBoton("Undo",  new Color(70, 70, 150), accionDeshacer);
        btnReiniciar = crearBoton("Reiniciar", new Color(70, 110, 70), accionReiniciar);

        add(etNivel);
        add(separador());
        add(etMovimientos);
        add(etEmpujes);
        add(etUndosDisponibles);
        add(separador());
        add(btnDeshacer);
        add(btnReiniciar);
    }

    /**
     * Actualiza todas las etiquetas del HUD con los datos actuales del historial.
     * Debe llamarse tras cada movimiento o undo.
     */
    public void actualizar(HistorialMovimientos historial, GestorNiveles gestorNiveles) {
        int indice = gestorNiveles.obtenerIndiceActual() + 1;
        int total  = gestorNiveles.obtenerTotalNiveles();
        etNivel           .setText("Nivel " + indice + " / " + total);
        etMovimientos     .setText("Movs: "          + historial.getTotalMovimientos());
        etEmpujes         .setText("Empujes: "        + historial.getTotalEmpujes());
        etUndosDisponibles.setText("Undos usados: "   + historial.getTotalUndos());

        // Deshabilitar el botón si ya no se puede deshacer
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
        btn.setFocusable(false);       // No roba el foco del JFrame (necesario para KeyListener)
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(130, 30));
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    private JLabel separador() {
        JLabel sep = new JLabel("│");
        sep.setForeground(new Color(80, 80, 80));
        return sep;
    }
}
