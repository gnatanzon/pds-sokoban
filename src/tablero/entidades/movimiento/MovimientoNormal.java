package tablero.entidades.movimiento;

import tablero.Tablero;
import tablero.entidades.Entidad;

public class MovimientoNormal implements EstrategiaMovimiento {

    @Override
    public ResultadoMovimiento mover(Tablero tablero, int filaOrigen, int colOrigen,
                                     int deltaFila, int deltaCol, Entidad entidad) {
        int destFila = filaOrigen + deltaFila;
        int destCol  = colOrigen  + deltaCol;

        if (!tablero.dentroDelBorde(destFila, destCol)) {
            return new ResultadoMovimiento(filaOrigen, colOrigen, false);
        }
        if (!tablero.obtenerCelda(destFila, destCol).estaLibre()) {
            return new ResultadoMovimiento(filaOrigen, colOrigen, false);
        }

        tablero.obtenerCelda(destFila, destCol).establecerEntidad(entidad);
        tablero.obtenerCelda(filaOrigen, colOrigen).limpiarEntidad();

        return new ResultadoMovimiento(destFila, destCol, true);
    }
}