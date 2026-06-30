package vista;

import controlador.GestorNiveles;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

//Panel que muestra los niveles disponibles
public class SelectorNivel extends JPanel {

    private static final Color COLOR_FONDO        = new Color(30, 30, 40);
    private static final Color COLOR_TITULO       = new Color(255, 215, 0);
    private static final Color COLOR_BTN_NORMAL   = new Color(55, 65, 95);
    private static final Color COLOR_BTN_HOVER    = new Color(80, 100, 160);
    private static final Color COLOR_BTN_TEXTO    = Color.WHITE;
    private static final Color COLOR_SUBTITULO    = new Color(180, 180, 200);

    private final GestorNiveles gestor;
    private final Consumer<String> onNivelSeleccionado;
    private final Runnable onVolverMenu;

    public SelectorNivel(GestorNiveles gestor, Consumer<String> onNivelSeleccionado, Runnable onVolverMenu) {
        this.gestor = gestor;
        this.onNivelSeleccionado = onNivelSeleccionado;
        this.onVolverMenu = onVolverMenu;
        construirUI();
    }

    private void construirUI() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(40, 60, 40, 60));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelNiveles(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("SOKOBAN", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        titulo.setForeground(COLOR_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Seleccioná un nivel para comenzar", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(COLOR_SUBTITULO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // instrucciones
        JPanel cuadroInstrucciones = new JPanel(new BorderLayout());
        cuadroInstrucciones.setBackground(new Color(40, 40, 20)); // Fondo oscuro levemente amarillento
        cuadroInstrucciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_TITULO, 1), // Borde amarillo
                BorderFactory.createEmptyBorder(6, 16, 6, 16)    // Margen interno
        ));

        cuadroInstrucciones.setMaximumSize(new Dimension(320, 32));
        cuadroInstrucciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instrucciones = new JLabel("Usá las flechas o WASD para mover al jugador", SwingConstants.CENTER);
        instrucciones.setFont(new Font("SansSerif", Font.ITALIC, 12));
        instrucciones.setForeground(COLOR_TITULO);
        cuadroInstrucciones.add(instrucciones, BorderLayout.CENTER);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(cuadroInstrucciones);
        panel.add(Box.createVerticalStrut(16));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(24));

        return panel;
    }

    private JScrollPane crearPanelNiveles() {
        List<String> niveles = gestor.obtenerNiveles();

        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);

        for (int i = 0; i < niveles.size(); i++) {
            String ruta = niveles.get(i);
            grid.add(crearBotonNivel(i + 1, ruta));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JButton crearBotonNivel(int numero, String rutaNivel) {
        boolean desbloqueado = gestor.estaDesbloqueado(rutaNivel);
        String nombre = gestor.obtenerNombreNivel(rutaNivel);

        String etiqueta;
        if (desbloqueado) {
            etiqueta = "<html><center>🎮<br>" + nombre + "</center></html>";
        }  else {
            etiqueta = "<html><center>🔒<br>" + nombre + "</center></html>";
        }

        JButton btn = new JButton(etiqueta);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setForeground(COLOR_BTN_TEXTO);
        btn.setBackground(COLOR_BTN_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 80));
        btn.setEnabled(desbloqueado);

        if (!desbloqueado) {
            btn.setToolTipText("Completa el nivel anterior para desbloquearlo");
        }

        // Efecto hover con el mouse
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (desbloqueado) btn.setBackground(COLOR_BTN_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (desbloqueado) btn.setBackground(COLOR_BTN_NORMAL);
            }
        });

        btn.addActionListener(e -> onNivelSeleccionado.accept(rutaNivel));
        return btn;
    }

    private JPanel crearPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnVolver = new JButton("Menú Principal");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVolver.setBackground(new Color(80, 80, 80));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(150, 35));

        btnVolver.addActionListener(e -> onVolverMenu.run());

        panel.add(btnVolver);

        return panel;
    }
}
