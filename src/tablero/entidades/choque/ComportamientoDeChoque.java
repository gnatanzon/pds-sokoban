package tablero.entidades.choque;

import tablero.Tablero;
import tablero.entidades.Caja;

/**
 * Strategy: define cómo reacciona una caja ante distintos eventos de choque.
 */
public interface ComportamientoDeChoque {

    /** Se ejecuta cuando la caja fue empujada con éxito a una nueva celda. */
    void alSerEmpujada(Tablero tablero, int fila, int columna, Caja caja);

    /** Se ejecuta cuando la caja NO pudo moverse porque chocó contra un obstáculo sólido. */
    default void alChocarConObstaculo(Tablero tablero, int fila, int columna, Caja caja) {
        // Por defecto no hace nada
    }

    /** Se ejecuta cuando una caja vecina detona o se rompe junto a esta caja. */
    default void alDetonarVecino(Tablero tablero, int fila, int columna, Caja caja) {
        // Por defecto no hace nada
    }
}
