package tablero.entidades.movimiento;

import tablero.Tablero;
import tablero.entidades.Entidad;


public class MovimientoResbaladizo implements EstrategiaMovimiento {

    @Override
    public ResultadoMovimiento mover(Tablero tablero, int filaOrigen, int colOrigen,
                                     int deltaFila, int deltaCol, Entidad entidad) {
        // Primero verificamos que la celda destino inmediata esté libre
        int destFila = filaOrigen + deltaFila;
        int destCol  = colOrigen  + deltaCol;

        if (!tablero.dentroDelBorde(destFila, destCol)) {
            return new ResultadoMovimiento(filaOrigen, colOrigen, false);
        }
        if (!tablero.obtenerCelda(destFila, destCol).estaLibre()) {
            return new ResultadoMovimiento(filaOrigen, colOrigen, false);
        }

        // Deslizamos mientras el piso siguiente sea resbaladizo y esté libre
        tablero.obtenerCelda(filaOrigen, colOrigen).limpiarEntidad();

        int filaActual = destFila;
        int colActual  = destCol;

        while (true) {
            tablero.obtenerCelda(filaActual, colActual).establecerEntidad(entidad);

            int siguienteFila = filaActual + deltaFila;
            int siguienteCol  = colActual  + deltaCol;

            boolean puedeAvanzar = tablero.dentroDelBorde(siguienteFila, siguienteCol)
                    && tablero.obtenerCelda(siguienteFila, siguienteCol).estaLibre()
                    && tablero.obtenerCelda(filaActual, colActual).obtenerPiso().permiteDeslizamiento();

            if (!puedeAvanzar) break;

            tablero.obtenerCelda(filaActual, colActual).limpiarEntidad();
            filaActual = siguienteFila;
            colActual  = siguienteCol;
        }

        return new ResultadoMovimiento(filaActual, colActual, true);
    }
}