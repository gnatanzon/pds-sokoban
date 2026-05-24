package vista;

import tablero.Tablero;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private final PanelTablero panelTablero;

    public VentanaPrincipal(Tablero tablero) {
        CargadorAssets assets = new CargadorAssets();
        this.panelTablero = new PanelTablero(tablero, assets);

        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(panelTablero);

        pack();
        setLocationRelativeTo(null); // centra la ventana en la pantalla
        setVisible(true);
    }

    public PanelTablero obtenerPanelTablero() {
        return panelTablero;
    }
}