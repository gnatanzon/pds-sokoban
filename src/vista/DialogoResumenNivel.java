package vista;

import controlador.GestorNiveles;
import controlador.memento.CalculadorPuntaje;
import controlador.memento.HistorialMovimientos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoResumenNivel extends JDialog {

    // ── Colores y fuentes ────────────────────────────────────────────────────
    private static final Color COLOR_FONDO      = new Color(20, 20, 30);
    private static final Color COLOR_TITULO     = new Color(255, 210, 50);
    private static final Color COLOR_TEXTO      = new Color(210, 210, 210);
    private static final Color COLOR_OPTIMO     = new Color(80, 200, 120);
    private static final Color COLOR_PUNTAJE    = new Color(100, 220, 255);
    private static final Font  FUENTE_TITULO    = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font  FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD,  18);
    private static final Font  FUENTE_STAT      = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font  FUENTE_BOTON     = new Font("Segoe UI", Font.BOLD,  14);

   //acciones a realizar cuando se cierre el resumen
    public enum Accion { SIGUIENTE_NIVEL, REINICIAR, MENU_PRINCIPAL }

    private Accion accionElegida = Accion.MENU_PRINCIPAL;

    public DialogoResumenNivel(Frame padre, HistorialMovimientos historial,
                               int puntaje, GestorNiveles gestorNiveles,
                               int movimientosMinimos) {
        super(padre, "¡Nivel Superado!", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // forzar eleccion
        construirContenido(historial, puntaje, gestorNiveles, movimientosMinimos);
    }

    // ── Construcción del contenido ───────────────────────────────────────────

    private void construirContenido(HistorialMovimientos historial, int puntaje,
                                    GestorNiveles gestorNiveles, int movimientosMinimos) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(28, 48, 28, 48));

        panel.add(centrar(etiqueta("¡Nivel Superado!", FUENTE_TITULO, COLOR_TITULO)));
        panel.add(espacio(8));

        // estrellas - puntaje
        String estrellas = calcularEstrellas(puntaje);
        if (!estrellas.isEmpty()) {
            panel.add(centrar(etiqueta(estrellas, new Font("Dialog", Font.PLAIN, 28), COLOR_TITULO)));
            panel.add(espacio(12));
        }

        panel.add(crearLinea());
        panel.add(espacio(12));

        // estadísticas
        panel.add(filaEstadistica("Movimientos realizados:",
                String.valueOf(historial.getTotalMovimientos())));

        if (movimientosMinimos > 0) {
            int extra = historial.getTotalMovimientos() - movimientosMinimos;
            String descripcion = (extra <= 0)
                    ? "Cantidad óptima (" + movimientosMinimos + ") mín."
                    : "+" + extra + " sobre el mínimo (" + movimientosMinimos + ")";
            Color colorExtra = (extra <= 0) ? COLOR_OPTIMO : new Color(220, 150, 60);
            panel.add(filaEstadistica("   Referencia:", descripcion, colorExtra));
        }

        panel.add(espacio(6));
        panel.add(filaEstadistica("Empujes efectuados:",
                String.valueOf(historial.getTotalEmpujes())));
        panel.add(espacio(6));
        panel.add(filaEstadistica("Undo realizados:",
                String.valueOf(historial.getTotalUndos())));

        panel.add(espacio(16));
        panel.add(crearLinea());
        panel.add(espacio(12));

        // puntaje final
        panel.add(centrar(etiqueta(
                "Puntaje: " + puntaje + " / " + CalculadorPuntaje.getPuntajeMaximo(),
                FUENTE_SUBTITULO, COLOR_PUNTAJE)));

        panel.add(espacio(24));

        panel.add(centrar(crearPanelBotones(gestorNiveles)));

        setContentPane(panel);
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private JPanel crearPanelBotones(GestorNiveles gestorNiveles) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setBackground(COLOR_FONDO);

        if (gestorNiveles.hayNivelSiguiente()) {
            JButton btnSiguiente = crearBoton("Siguiente", new Color(0, 130, 0));
            btnSiguiente.addActionListener(e -> elegir(Accion.SIGUIENTE_NIVEL));
            panelBotones.add(btnSiguiente);
        }

        JButton btnReiniciar = crearBoton("Repetir", new Color(60, 60, 150));
        btnReiniciar.addActionListener(e -> elegir(Accion.REINICIAR));
        panelBotones.add(btnReiniciar);

        JButton btnMenu = crearBoton("Menú Principal", new Color(80, 80, 80));
        btnMenu.addActionListener(e -> elegir(Accion.MENU_PRINCIPAL));
        panelBotones.add(btnMenu);

        return panelBotones;
    }

    private void elegir(Accion accion) {
        accionElegida = accion;
        dispose();
    }

    // ── Helpers visuales ────────────────────────────────────────────────────

    private JLabel etiqueta(String texto, Font fuente, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(color);
        return label;
    }

    private JPanel centrar(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setBackground(COLOR_FONDO);
        p.add(comp);
        return p;
    }

    private JPanel filaEstadistica(String clave, String valor) {
        return filaEstadistica(clave, valor, COLOR_TITULO);
    }

    private JPanel filaEstadistica(String clave, String valor, Color colorValor) {
        JPanel fila = new JPanel(new BorderLayout(24, 0));
        fila.setBackground(COLOR_FONDO);
        fila.setMaximumSize(new Dimension(380, 26));

        JLabel lblClave = etiqueta(clave, FUENTE_STAT, COLOR_TEXTO);
        JLabel lblValor = etiqueta(valor, FUENTE_STAT, colorValor);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblClave, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 38));
        return btn;
    }

    private Component espacio(int altura) {
        return Box.createVerticalStrut(altura);
    }

    private JSeparator crearLinea() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(60, 60, 80));
        sep.setMaximumSize(new Dimension(380, 2));
        return sep;
    }

    private String calcularEstrellas(int puntaje) {
        int max = CalculadorPuntaje.getPuntajeMaximo();
        if (puntaje >= (int)(max * 0.9)) return "\u2605\u2605\u2605";
        if (puntaje >= (int)(max * 0.6)) return "\u2605\u2605";
        if (puntaje >= (int)(max * 0.3)) return "\u2605";
        return "";
    }

    // ── Acceso al resultado ──────────────────────────────────────────────────

    public Accion getAccionElegida() {
        return accionElegida;
    }
}
