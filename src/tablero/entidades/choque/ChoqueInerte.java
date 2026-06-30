package tablero.entidades.choque;

import tablero.Tablero;
import tablero.entidades.Caja;

/** Comportamiento para CajaNormal y CajaLlave: no reacciona a los choques. */
public class ChoqueInerte implements ComportamientoDeChoque {

    @Override
    public void alSerEmpujada(Tablero tablero, int fila, int columna, Caja caja) {
        // Inerte: no hace nada
    }
}