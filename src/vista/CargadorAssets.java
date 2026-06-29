package vista;

import tablero.Celda;
import tablero.background.*;
import tablero.entidades.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CargadorAssets {

    private static final int TAMANO_CELDA = 64;

    private final Map<Class<?>, ImageIcon> imagenes = new HashMap<>();

    public CargadorAssets(Jugador jugador) {
        cargarImagen(Pared.class,           "Pared.png");
        cargarImagen(EspacioVacio.class,    "EspacioVacio.PNG");
        cargarImagen(Destino.class,         "Destino.PNG");
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
        String ruta = "Assets/" + nombreArchivo;

        ImageIcon icono = new ImageIcon(ruta);

        if (icono.getIconWidth() > 0) {
            Image imagen = icono.getImage()
                    .getScaledInstance(TAMANO_CELDA, TAMANO_CELDA, Image.SCALE_SMOOTH);

            imagenes.put(clase, new ImageIcon(imagen));
        } else {
            System.err.println("No se encontró el asset: " + ruta);
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