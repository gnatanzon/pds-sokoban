package vista;

import controlador.ControladorJuego;
import tablero.Tablero;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private final PanelTablero panelTablero;

    public VentanaPrincipal(Tablero tablero) {
        CargadorAssets assets = new CargadorAssets();
        this.panelTablero = new PanelTablero(tablero, assets);

        ControladorJuego controlador = new ControladorJuego(tablero, panelTablero::repaint);
        addKeyListener(controlador);

        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(panelTablero);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public PanelTablero obtenerPanelTablero() {
        return panelTablero;
    }
}
