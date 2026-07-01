package tablero.entidades.choque;

import tablero.Celda;
import tablero.Tablero;
import tablero.entidades.Caja;
import tablero.entidades.CajaFragil;

public class ChoqueFragil implements ComportamientoDeChoque {

    @Override
    public void alSerEmpujada(Tablero tablero, int fila, int columna, Caja caja) {
        caja.recibirEmpuje();

        if (caja.estaRota()) {
            tablero.obtenerCelda(fila, columna).limpiarEntidad();
            notificarVecinos(tablero, fila, columna);
        }
    }


    private void notificarVecinos(Tablero tablero, int fila, int columna) {
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) continue; //cambiar logica para no usar continue

                int f = fila + df;
                int c = columna + dc;

                if (!tablero.dentroDelBorde(f, c)) continue;

                Celda vecina = tablero.obtenerCelda(f, c);
                if (vecina.tieneEntidad() && vecina.obtenerEntidad().esCaja()) {
                    vecina.obtenerEntidad().comoCaja().alDetonarVecino(tablero, f, c);
                }
            }
        }
    }
}
