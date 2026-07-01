package vista;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Pantalla de inicio del juego
 */
public class VentanaInicio extends JDialog {

    private static final Color COLOR_FONDO  = new Color(30, 30, 40);
    private static final Color COLOR_BORDE  = new Color(60, 60, 80);
    private static final Color COLOR_SUB    = new Color(180, 180, 200);

    private boolean jugarSeleccionado = false;

    public VentanaInicio(JFrame parent) {
        super(parent, "Sokoban", true);

        setUndecorated(true);
        initUI();

        setSize(560, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);
        ((JPanel) getContentPane()).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        add(crearCentro(),       BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearCentro() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        panel.add(Box.createVerticalGlue());

        panel.add(crearMarcoLogo());

        panel.add(Box.createVerticalStrut(16));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearMarcoLogo() {
        JPanel marco = new JPanel(new BorderLayout());
        marco.setBackground(new Color(40, 40, 50));
        marco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 2),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        marco.setAlignmentX(Component.CENTER_ALIGNMENT);
        marco.setMaximumSize(new Dimension(470, 150));

        JLabel logo = new JLabel(cargarLogo(), SwingConstants.CENTER);
        marco.add(logo, BorderLayout.CENTER);

        return marco;
    }

    private ImageIcon cargarLogo() {
        URL url = getClass().getResource("/LogoSokoban.png");
        if (url == null) {
            return null;
        }
        Image img = new ImageIcon(url).getImage()
                .getScaledInstance(420, -1, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 40, 0));

        JButton btnJugar = boton("Jugar", new Color(34, 139, 34));
        btnJugar.setPreferredSize(new Dimension(240, 50));
        btnJugar.setMaximumSize(new Dimension(240, 50));
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.addActionListener(e -> confirmarJugar());

        JButton btnSalir = boton("Salir", new Color(186, 48, 48));
        btnSalir.setPreferredSize(new Dimension(240, 40));
        btnSalir.setMaximumSize(new Dimension(240, 40));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnSalir);

        return panel;
    }

    private void confirmarJugar() {
        jugarSeleccionado = true;
        dispose();
    }

    public boolean isJugarSeleccionado() {
        return jugarSeleccionado;
    }

    private JButton boton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}