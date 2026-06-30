package tablero.entidades.movimiento;

import tablero.Tablero;
import tablero.entidades.Entidad;


public class MovimientoResbaladizo implements EstrategiaMovimiento {

    @Override
    public ResultadoMovimiento mover(Tablero tablero, int filaOrigen, int colOrigen,
                                     int deltaFila, int deltaCol, Entidad entidad) {

        int destFila = filaOrigen + deltaFila;
        int destCol  = colOrigen  + deltaCol;

        if (!tablero.dentroDelBorde(destFila, destCol)
                || !tablero.obtenerCelda(destFila, destCol).estaLibre()) {
            return new ResultadoMovimiento(filaOrigen, colOrigen, false);
        }

        boolean disparaDeslizamiento =
                tablero.obtenerCelda(destFila, destCol).obtenerPiso().permiteDeslizamiento();

        int filaFinal = destFila;
        int colFinal  = destCol;

        if (disparaDeslizamiento) {
            while (true) {
                int filaSiguiente = filaFinal + deltaFila;
                int colSiguiente  = colFinal  + deltaCol;

                boolean puedeAvanzar = tablero.dentroDelBorde(filaSiguiente, colSiguiente)
                        && tablero.obtenerCelda(filaSiguiente, colSiguiente).estaLibre();

                if (!puedeAvanzar) break;

                filaFinal = filaSiguiente;
                colFinal  = colSiguiente;
            }
        }

        tablero.obtenerCelda(filaOrigen, colOrigen).limpiarEntidad();
        tablero.obtenerCelda(filaFinal, colFinal).establecerEntidad(entidad);

        return new ResultadoMovimiento(filaFinal, colFinal, true);
    }
}