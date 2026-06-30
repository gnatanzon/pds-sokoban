package vista;

import tablero.Celda;
import tablero.Tablero;

import javax.swing.*;
import java.awt.*;

public class PanelTablero extends JPanel {

    private Tablero tablero;
    private final CargadorAssets assets;
    private final int tamanoCelda;

    public PanelTablero(Tablero tablero, CargadorAssets assets) {
        this.tablero = tablero;
        this.assets = assets;
        this.tamanoCelda = assets.obtenerTamanoCelda();
        ajustarTamano();

        setOpaque(false);
    }

    private void ajustarTamano() {
        int ancho = tablero.obtenerColumnas() * tamanoCelda;
        int alto = tablero.obtenerFilas() * tamanoCelda;
        setPreferredSize(new Dimension(ancho, alto));
    }

    public void actualizarTablero(Tablero tablero) {
        this.tablero = tablero;
        ajustarTamano();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                int x = c * tamanoCelda;
                int y = f * tamanoCelda;
                Celda celda = tablero.obtenerCelda(f, c);

                // Dibuja el piso
                ImageIcon imgPiso = assets.obtenerImagenPiso(celda);
                if (imgPiso != null) {
                    g.drawImage(imgPiso.getImage(), x, y, tamanoCelda, tamanoCelda, this);
                }

                // Dibuja la entidad encima del piso (si hay)
                ImageIcon imgEntidad = assets.obtenerImagenEntidad(celda);
                if (imgEntidad != null) {
                    g.drawImage(imgEntidad.getImage(), x, y, tamanoCelda, tamanoCelda, this);
                }
            }
        }
    }
}