package tablero.entidades.movimiento;

import tablero.Tablero;
import tablero.entidades.Entidad;

public interface EstrategiaMovimiento {
    /**
     * Mueve la entidad desde (filaOrigen, colOrigen) en la dirección dada.
     * Devuelve la celda final donde quedó la entidad.
     */
    ResultadoMovimiento mover(Tablero tablero, int filaOrigen, int colOrigen,
                              int deltaFila, int deltaCol, Entidad entidad);
}
