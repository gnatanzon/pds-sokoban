package vista;

import tablero.entidades.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PantallaElegirPersonaje extends JDialog {

    private Jugador jugador = null;

    private JTextField campoNombre;
    private JComboBox<String> comboSprite;
    private JComboBox<String> comboSonido;
    private JLabel labelPreview;

    private static final String[] OPCIONES_SPRITE = {
            "Humano", "Ottoman", "Otto"
    };
    private static final String[] OPCIONES_SONIDO = {
            "Sonido Gnérico", "Sonido Robot", "Sonido Gatito"
    };


    private static final Color COLOR_FONDO  = new Color(30, 30, 40);
    private static final Color COLOR_TITULO = new Color(255, 215, 0);
    private static final Color COLOR_TEXTO  = Color.WHITE;

    public PantallaElegirPersonaje(JFrame parent) {
<<<<<<< Updated upstream
        super(parent, "Elegir Personaje", true);
=======
        super(parent, "Crear Personaje", true);
>>>>>>> Stashed changes
        setUndecorated(true);
        initUI();

        setSize(650, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        ((JPanel)getContentPane()).setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));

        add(crearEncabezado(),   BorderLayout.NORTH);
        add(crearFormulario(),   BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));

        JLabel titulo = new JLabel("SOKOBAN", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        titulo.setForeground(COLOR_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("¡Crea tu personaje!", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitulo.setForeground(new Color(180, 180, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(12, 15, 12, 15);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.2;
        panel.add(label("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        campoNombre = new JTextField("Jugador");
        campoNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        campoNombre.setBackground(new Color(50, 50, 60));
        campoNombre.setForeground(Color.WHITE);
        campoNombre.setCaretColor(Color.WHITE);
        campoNombre.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(campoNombre, gbc);

        // sprite
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.2;
        panel.add(label("Personaje:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        comboSprite = new JComboBox<>(OPCIONES_SPRITE);
        aplicarEstiloOscuro(comboSprite);
        comboSprite.setFont(new Font("SansSerif", Font.BOLD, 13));
        comboSprite.addActionListener(e -> actualizarPreview());
        panel.add(comboSprite, gbc);

        // preview del personaje
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        labelPreview = new JLabel("", SwingConstants.CENTER);
        labelPreview.setPreferredSize(new Dimension(140, 140));
        labelPreview.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        labelPreview.setBackground(new Color(40, 40, 50));
        labelPreview.setOpaque(true);
        panel.add(labelPreview, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        actualizarPreview();

        // sonido
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.2;
        panel.add(label("Sonido:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        comboSonido = new JComboBox<>(OPCIONES_SONIDO);
        aplicarEstiloOscuro(comboSonido);
        comboSonido.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(comboSonido, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 40, 0));

        JButton btnJugar = boton("¡Jugar!", new Color(34, 139, 34));
        btnJugar.setPreferredSize(new Dimension(240, 50));
        btnJugar.setMaximumSize(new Dimension(240, 50));
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.addActionListener(e -> confirmar());

        JButton btnSalir = boton("Salir", new Color(219, 52, 52));
        btnSalir.setPreferredSize(new Dimension(240, 40));
        btnSalir.setMaximumSize(new Dimension(240, 40));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnSalir);

        return panel;
    }

    private void actualizarPreview() {
        int opcion = comboSprite.getSelectedIndex() + 1;
        String nombre = "jugador" + opcion + ".png";
        URL url = getClass().getResource("/" + nombre);
        if (url != null) {
            // Escalamos la imagen para que acompañe el nuevo tamaño del preview
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            labelPreview.setIcon(new ImageIcon(img));
            labelPreview.setText("");
        } else {
            labelPreview.setIcon(null);
            labelPreview.setForeground(Color.LIGHT_GRAY);
            labelPreview.setFont(new Font("SansSerif", Font.PLAIN, 12));
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

    public Jugador getJugador() { return jugador; }

    private JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 16)); // Etiqueta más grande
        l.setForeground(COLOR_TEXTO);
        return l;
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

    private void aplicarEstiloOscuro(JComboBox<String> combo) {
        combo.setBackground(new Color(50, 50, 60));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("SansSerif", Font.BOLD, 13));

        //estilo lista desplegable
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(new Color(80, 80, 100));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(50, 50, 60));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }
}