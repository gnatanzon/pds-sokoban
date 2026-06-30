package vista;

import controlador.GestorNiveles;
import controlador.memento.HistorialMovimientos;

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

        add(etNivel);
        add(separador());
        add(etMovimientos);
        add(etEmpujes);
        add(etUndosDisponibles);
        add(separador());
        add(btnDeshacer);
        add(btnReiniciar);
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
