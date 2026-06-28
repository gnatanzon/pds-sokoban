package controlador.memento;

import tablero.Celda;
import tablero.Tablero;
import tablero.background.Piso;
import tablero.entidades.Entidad;

/**
 * Memento: guarda una copia profunda del tablero en un instante dado.
 */
public class EstadoTablero {

    private final Piso[][]    pisos;
    private final Entidad[][] entidades;

    public EstadoTablero(Tablero tablero) {
        int filas = tablero.obtenerFilas();
        int cols  = tablero.obtenerColumnas();
        pisos     = new Piso[filas][cols];
        entidades = new Entidad[filas][cols];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                pisos[f][c]     = celda.obtenerPiso();
                entidades[f][c] = celda.obtenerEntidad();
            }
        }
    }

    public Piso[][] getPisos()         { return pisos; }
    public Entidad[][] getEntidades()  { return entidades; }
}