package vista;

import tablero.entidades.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PantallaCrearPersonaje extends JDialog {

    private Jugador jugador = null;

    private JTextField campoNombre;
    private JComboBox<String> comboSprite;
    private JComboBox<String> comboSonido;
    private JLabel labelPreview;

    private static final String[] OPCIONES_SPRITE = {
            "Jugador 1", "Jugador 2", "Jugador 3"
    };
    private static final String[] OPCIONES_SONIDO = {
            "Sonido 1", "Sonido 2", "Sonido 3"
    };

    public PantallaCrearPersonaje(JFrame parent) {
        super(parent, "Crear Personaje", true);
        initUI();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        // Impide cerrar sin confirmar (X cierra → jugador queda null → System.exit en VentanaPrincipal)
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 60));

        add(crearTitulo(),      BorderLayout.NORTH);
        add(crearFormulario(),  BorderLayout.CENTER);
        add(crearBotones(),     BorderLayout.SOUTH);
    }

    private JLabel crearTitulo() {
        JLabel titulo = new JLabel("¡Crea tu personaje!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        return titulo;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(label("Nombre:"), gbc);
        gbc.gridx = 1;
        campoNombre = new JTextField("Jugador", 15);
        campoNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(campoNombre, gbc);

        // Sprite
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(label("Personaje:"), gbc);
        gbc.gridx = 1;
        comboSprite = new JComboBox<>(OPCIONES_SPRITE);
        comboSprite.addActionListener(e -> actualizarPreview());
        panel.add(comboSprite, gbc);

        // Preview
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        labelPreview = new JLabel("", SwingConstants.CENTER);
        labelPreview.setPreferredSize(new Dimension(80, 80));
        labelPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelPreview.setBackground(new Color(50, 50, 80));
        labelPreview.setOpaque(true);
        panel.add(labelPreview, gbc);
        actualizarPreview();

        // Sonido
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label("Sonido:"), gbc);
        gbc.gridx = 1;
        comboSonido = new JComboBox<>(OPCIONES_SONIDO);
        panel.add(comboSonido, gbc);

        return panel;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(30, 30, 60));

        JButton btnJugar = boton("¡Jugar!", new Color(0, 150, 60));
        btnJugar.addActionListener(e -> confirmar());
        panel.add(btnJugar);

        return panel;
    }

    private void actualizarPreview() {
        int opcion = comboSprite.getSelectedIndex() + 1;
        String nombre = "jugador" + opcion + ".png";
        // Usa el mismo mecanismo que CargadorAssets (getResource)
        URL url = getClass().getResource("/" + nombre);
        if (url != null) {
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            labelPreview.setIcon(new ImageIcon(img));
            labelPreview.setText("");
        } else {
            labelPreview.setIcon(null);
            labelPreview.setForeground(Color.LIGHT_GRAY);
            labelPreview.setFont(new Font("SansSerif", Font.PLAIN, 10));
            labelPreview.setText(nombre);
        }
    }

    private void confirmar() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresá un nombre.", "Nombre requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        jugador = new Jugador.Builder()
                .nombre(nombre)
                .sprite(comboSprite.getSelectedIndex() + 1)
                .sonido(comboSonido.getSelectedIndex() + 1)
                .build();

        dispose();
    }

    /** Devuelve el Jugador construido, o null si el usuario cerró sin confirmar. */
    public Jugador getJugador() { return jugador; }

    // ── Helpers ────────────────────────────────────────────────────────

    private JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(Color.WHITE);
        return l;
    }

    private JButton boton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}