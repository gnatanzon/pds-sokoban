package vista;

import controlador.ControladorJuego;
import controlador.GestorNiveles;
import controlador.memento.CalculadorPuntaje;
import tablero.CargadorNivel;
import tablero.Tablero;
import tablero.constructor.FabricaElementosSokoban;
import tablero.entidades.Jugador;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final String VISTA_SELECTOR = "selector";
    private static final String VISTA_JUEGO    = "juego";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private final GestorNiveles gestor;
    private PanelTablero panelTablero;
    private PanelHUD panelHUD;

    // El jugador se crea una sola vez al inicio y se reutiliza en todos los niveles
    private Jugador jugador;
    private Tablero tableroActual;

    public VentanaPrincipal() {
        this.gestor = new GestorNiveles();
        configurarVentana();
        mostrarPantallaCreacionPersonaje(); // primero el Builder, luego el selector
    }

    // ── Formato de la ventana principal ────────────────────────────────────────────────

    private void configurarVentana() {
        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(contenedor);
    }

    // ── Creación del personaje con el Builder ───────────────────────────────────────────

    private void mostrarPantallaCreacionPersonaje() {
        PantallaCrearPersonaje pantalla = new PantallaCrearPersonaje(this);
        pantalla.setVisible(true); // bloquea hasta que el usuario confirma o cancela

        jugador = pantalla.getJugador();

        if (jugador == null) {
            // El usuario cerró la ventana sin confirmar → salir
            System.exit(0);
        }

        setTitle("Sokoban — " + jugador.getNombre());
        mostrarSelector();
    }

    // ── Vistas disponibles ──────────────────────────────────────────────

    private void mostrarSelector() {
        SelectorNivel selector = new SelectorNivel(gestor, this::iniciarNivel);

        contenedor.add(selector, VISTA_SELECTOR);
        cardLayout.show(contenedor, VISTA_SELECTOR);

        pack();
        setMinimumSize(new Dimension(400, 380));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void iniciarNivel(String rutaNivel) {
        try {
            gestor.seleccionarNivel(rutaNivel);
            cargarYMostrarNivel(rutaNivel);
        } catch (Exception ex) {
            mostrarError("No se pudo cargar el nivel: " + ex.getMessage());
        }
    }

    private ControladorJuego controladorActual;

    private void cargarYMostrarNivel(String rutaNivel) throws Exception {
        FabricaElementosSokoban fabrica = new FabricaElementosSokoban(jugador);
        CargadorNivel cargador = new CargadorNivel(fabrica);
        Tablero tablero = cargador.cargar(rutaNivel);
        this.tableroActual = tablero;

        CargadorAssets assets = new CargadorAssets(jugador);
        panelTablero = new PanelTablero(tablero, assets);

        controladorActual = new ControladorJuego(tablero);

        panelHUD = new PanelHUD(
                () -> { if (controladorActual != null) controladorActual.deshacerMovimiento(); },
                this::reiniciarNivelActual
        );
        panelHUD.actualizar(controladorActual.getHistorial(), gestor);// HUD inicia con datos en cero

        // el observador actualiza el HUD
        controladorActual.agregarObservador(new controlador.observer.ObservadorJuego() {
            @Override public void onMovimiento() {
                panelTablero.repaint();
                panelHUD.actualizar(controladorActual.getHistorial(), gestor);
            }
            @Override public void onNivelSuperado() { SwingUtilities.invokeLater(VentanaPrincipal.this::mostrarDialogoVictoria); }
            @Override public void onUndo() {
                panelTablero.repaint();
                panelHUD.actualizar(controladorActual.getHistorial(), gestor);
            }
        });

        reemplazarVistaJuego(tablero, controladorActual);
    }

    private void reemplazarVistaJuego(Tablero tablero, ControladorJuego controlador) {
        for (java.awt.event.KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }

        JPanel panelJuego = construirPanelJuego();
        contenedor.add(panelJuego, VISTA_JUEGO);
        cardLayout.show(contenedor, VISTA_JUEGO);

        addKeyListener(controlador);
        pack();
        setLocationRelativeTo(null);
        requestFocus();
    }

    private JPanel construirPanelJuego() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(panelHUD, BorderLayout.NORTH);
        JPanel contenedorCentro = new JPanel(new GridBagLayout());
        contenedorCentro.setBackground(new Color(30, 30, 40));
        contenedorCentro.add(panelTablero);

        panel.add(contenedorCentro, BorderLayout.CENTER);

        // panel para volver al selector
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBackground(new Color(40, 40, 40));
        JButton btnSelector = crearBoton("Selector de niveles", this::volverAlSelector);
        panelSur.add(btnSelector);

        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    // ── Panel de botones ───────────────────────────────────────────


    private JButton crearBoton(String texto, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusable(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    // ── Ventana de victoria ──────────────────────────────────────────────────

    private void mostrarDialogoVictoria() {
        panelTablero.repaint();

        int mov    = controladorActual.getHistorial().getTotalMovimientos();
        int emp    = controladorActual.getHistorial().getTotalEmpujes();
        int undos  = controladorActual.getHistorial().getTotalUndos();
        int movMin = tableroActual.obtenerMovMin();
        int puntos = CalculadorPuntaje.calcular(mov, emp, undos, movMin);

        String mensaje = String.format("""
            ¡Nivel completado!
            
            Movimientos:  %d
            Empujes:      %d
            Undos usados: %d
            
            Puntaje final: %d
            """, mov, emp, undos, puntos);

        JOptionPane.showMessageDialog(this, mensaje, "Resumen", JOptionPane.INFORMATION_MESSAGE);

        boolean hayMasNiveles = gestor.hayNivelSiguiente();
        String nombreActual   = gestor.obtenerNombreNivel(gestor.obtenerNivelActual());

        JPanel contenidoDialogo = construirContenidoVictoria(nombreActual);

        String[] opciones = hayMasNiveles
                ? new String[]{"Siguiente nivel", "Repetir", "Selector"}
                : new String[]{"Repetir", "Selector"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                contenidoDialogo,
                "¡Nivel completado!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        manejarRespuestaVictoria(respuesta, hayMasNiveles);
    }

    private JPanel construirContenidoVictoria(String nombreNivel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));

        JLabel titulo = new JLabel("¡Nivel Superado!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(34, 139, 34));

        JLabel sub = new JLabel("Completaste " + nombreNivel + " ✓", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(new Color(80, 80, 80));

        // Muestra el nombre del personaje en la pantalla de victoria
        JLabel lblJugador = new JLabel("Jugador: " + jugador.getNombre(), SwingConstants.CENTER);
        lblJugador.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblJugador.setForeground(new Color(100, 100, 100));

        panel.add(titulo,     BorderLayout.NORTH);
        panel.add(sub,        BorderLayout.CENTER);
        panel.add(lblJugador, BorderLayout.SOUTH);
        return panel;
    }

    private void manejarRespuestaVictoria(int respuesta, boolean hayMasNiveles) {
        gestor.desbloquearSiguiente();
        if (hayMasNiveles) {
            switch (respuesta) {
                case 0 -> avanzarAlSiguienteNivel();
                case 1 -> reiniciarNivelActual();
                default -> volverAlSelector();
            }
        } else {
            if (respuesta == 0) reiniciarNivelActual();
            else                volverAlSelector();
        }
    }

    // ── Navegación entre niveles ───────────────────────────────────────────────

    private void avanzarAlSiguienteNivel() {
        try {
            String siguiente = gestor.avanzarNivelSiguiente();
            cargarYMostrarNivel(siguiente);
        } catch (Exception ex) {
            mostrarError("Error al cargar el siguiente nivel: " + ex.getMessage());
        }
    }

    private void reiniciarNivelActual() {
        try {
            cargarYMostrarNivel(gestor.obtenerNivelActual());
        } catch (Exception ex) {
            mostrarError("Error al reiniciar el nivel: " + ex.getMessage());
        }
    }

    private void volverAlSelector() {
        for (java.awt.event.KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }
        contenedor.removeAll();
        mostrarSelector();
    }

    // ─────────────────────────────────────────────────────────────

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}