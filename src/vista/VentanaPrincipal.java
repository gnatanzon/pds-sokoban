package vista;

import controlador.ControladorJuego;
import controlador.GestorNiveles;
import controlador.memento.CalculadorPuntaje;
import controlador.memento.HistorialMovimientos;
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
        setUndecorated(true); //chau ventana win
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        contenedor.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 2));
        setContentPane(contenedor);
    }

    // ── Creación del personaje con el Builder ───────────────────────────────────────────

    private void mostrarPantallaCreacionPersonaje() {
        PantallaElegirPersonaje pantalla = new PantallaElegirPersonaje(this);
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
        SelectorNivel selector = new SelectorNivel(gestor, this::iniciarNivel, this::volverAlMenuPrincipal);

        contenedor.add(selector, VISTA_SELECTOR);
        cardLayout.show(contenedor, VISTA_SELECTOR);

        setSize(650, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void volverAlMenuPrincipal() {
        contenedor.removeAll();
        contenedor.repaint();
        mostrarPantallaCreacionPersonaje();
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

        // El tamaño de la ventana ahora se ajusta al tablero real (en vez de
        // un 650x650 fijo), porque a partir del Nivel 3 los mapas son más
        // grandes que eso y el tablero quedaba recortado/inaccesible.
        ajustarTamanoVentana();
        setLocationRelativeTo(null);
        requestFocus();
    }

    private void ajustarTamanoVentana() {
        pack(); // calcula el tamaño real según el contenido (HUD + tablero + footer)

        Dimension minimo = new Dimension(650, 650);
        Dimension preferido = getSize();
        int ancho = Math.max(minimo.width, preferido.width);
        int alto  = Math.max(minimo.height, preferido.height);

        // No exceder el área visible de la pantalla
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        ancho = Math.min(ancho, pantalla.width - 40);
        alto  = Math.min(alto, pantalla.height - 40);

        setSize(ancho, alto);
    }

    private void ajustarTamanoVentana(Tablero tablero) {
        int tamanoCelda = 64; // mismo valor que CargadorAssets.TAMANO_CELDA
        int anchoTablero = tablero.obtenerColumnas() * tamanoCelda;
        int altoTablero   = tablero.obtenerFilas()    * tamanoCelda;

        int anchoMinimo = 650;
        int altoExtra    = 140; // espacio reservado para HUD + panel inferior

        setSize(Math.max(anchoMinimo, anchoTablero + 20), altoTablero + altoExtra);
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 30));
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    // ── Ventana de victoria ──────────────────────────────────────────────────

    private void mostrarDialogoVictoria() {
        panelTablero.repaint();

        // 1. Obtener historial y calcular puntaje
        HistorialMovimientos historial = controladorActual.getHistorial();
        int movMin = tableroActual.obtenerMovMin();
        int puntos = CalculadorPuntaje.calcular(
                historial.getTotalMovimientos(),
                historial.getTotalEmpujes(),
                historial.getTotalUndos(),
                movMin
        );

        // 2. Desbloquear el nivel siguiente (ya que este fue superado)
        gestor.desbloquearSiguiente();

        // 3. Crear y mostrar el diálogo modal (la ejecución se pausa acá hasta que el jugador hace clic)
        DialogoResumenNivel dialogo = new DialogoResumenNivel(
                this,
                historial,
                puntos,
                gestor,
                movMin
        );
        dialogo.setVisible(true);

        // 4. Leer qué botón presionó y ejecutar la acción
        switch (dialogo.getAccionElegida()) {
            case SIGUIENTE_NIVEL -> avanzarAlSiguienteNivel();
            case REINICIAR       -> reiniciarNivelActual();
            case MENU_PRINCIPAL  -> volverAlSelector();
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