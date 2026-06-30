package tablero.entidades.choque;

import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.background.Piso;
import tablero.entidades.Caja;

public class ChoqueExplosivo implements ComportamientoDeChoque {

    @Override
    public void alSerEmpujada(Tablero tablero, int fila, int columna, Caja caja) {
        // Al ser empujada con éxito (cae en piso libre) no detona, solo se mueve
    }

    @Override
    public void alChocarConObstaculo(Tablero tablero, int fila, int columna, Caja caja) {
        detonar(tablero, fila, columna);
    }

    @Override
    public void alDetonarVecino(Tablero tablero, int fila, int columna, Caja caja) {
        detonar(tablero, fila, columna);
    }

    private void detonar(Tablero tablero, int fila, int columna) {
        //GestorSonido.obtenerInstancia().reproducir(GestorSonido.Sonido.EXPLOSION);

        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) continue;

                int f = fila + df;
                int c = columna + dc;

                if (!tablero.dentroDelBorde(f, c)) continue;
                if (esBordeDelTablero(tablero, f, c)) continue; // seguro extra: el borde nunca vuela

                Celda celda = tablero.obtenerCelda(f, c);
                Piso piso = celda.obtenerPiso();

                if (piso.esDestructible()) {
                    celda.establecerPiso(piso.destruir());
                }
            }
        }

        // La caja explosiva se consume en su propia detonación
        tablero.obtenerCelda(fila, columna).limpiarEntidad();
    }

    private boolean esBordeDelTablero(Tablero tablero, int fila, int columna) {
        return fila == 0 || columna == 0
                || fila == tablero.obtenerFilas() - 1
                || columna == tablero.obtenerColumnas() - 1;
    }
}
