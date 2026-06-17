package vista;

import tablero.Celda;
import tablero.background.*;
import tablero.entidades.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CargadorAssets {

    private static final int TAMANO_CELDA = 64;

    private final Map<Class<?>, ImageIcon> imagenes = new HashMap<>();

    public CargadorAssets(Jugador jugador) {
        cargarImagen(Pared.class,           "Pared.png");
        cargarImagen(EspacioVacio.class,    "EspacioVacio.png");
        cargarImagen(Destino.class,         "Destino.png");
        cargarImagen(PisoResbaladizo.class, "PisoResbaladizo.png");
        cargarImagen(Cerrojo.class,         "Cerrojo.png");
        cargarImagen(MuroCerrado.class,     "MuroCerrado.png");
        cargarImagen(MuroAbierto.class,     "MuroAbierto.png");

        // Usa el sprite elegido por el jugador en lugar de "Jugador.png" fijo
        cargarImagen(Jugador.class,         jugador.getSpritePath());
        cargarImagen(CajaNormal.class,      "CajaNormal.png");
        cargarImagen(CajaFragil.class,      "CajaFragil.png");
        cargarImagen(CajaLlave.class,       "CajaLlave.png");
    }

    private void cargarImagen(Class<?> clase, String nombreArchivo) {
        URL url = getClass().getResource("/" + nombreArchivo);
        if (url != null) {
            Image imagen = new ImageIcon(url).getImage()
                    .getScaledInstance(TAMANO_CELDA, TAMANO_CELDA, Image.SCALE_SMOOTH);
            imagenes.put(clase, new ImageIcon(imagen));
        } else {
            System.err.println("No se encontró el asset: " + nombreArchivo);
        }
    }

    public ImageIcon obtenerImagenPiso(Celda celda) {
        return imagenes.get(celda.obtenerPiso().getClass());
    }

    public ImageIcon obtenerImagenEntidad(Celda celda) {
        if (celda.tieneEntidad()) {
            return imagenes.get(celda.obtenerEntidad().getClass());
        }
        return null;
    }

    public int obtenerTamanoCelda() { return TAMANO_CELDA; }
}