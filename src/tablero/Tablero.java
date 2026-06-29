package tablero;

import tablero.background.Piso;


public class Tablero {

    private final Celda[][] celdas;
    private final int filas;
    private final int columnas;

    public Tablero(Celda[][] celdas) {
        this.celdas = celdas;
        this.filas = celdas.length;
        this.columnas = celdas[0].length;
    }

    public Celda obtenerCelda(int fila, int columna) {
        return celdas[fila][columna];
    }

    public int obtenerFilas() {
        return filas;
    }

    public int obtenerColumnas() {
        return columnas;
    }

    public boolean dentroDelBorde(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public void imprimir() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                System.out.print("[" + celdas[f][c] + "] ");
            }
            System.out.println();
        }
    }
    public void abrirMuros() {
        for (int f = 0; f < obtenerFilas(); f++) {
            for (int c = 0; c < obtenerColumnas(); c++) {
                Celda celda = obtenerCelda(f, c);
                Piso piso = celda.obtenerPiso();

                if (piso.debeAbrirse()) {
                    celda.establecerPiso(piso.abrir());
                }
            }
        }
    }

    public boolean todosLosDestinosTienenCaja() {
        for (int f = 0; f < obtenerFilas(); f++) {
            for (int c = 0; c < obtenerColumnas(); c++) {
                Celda celda = obtenerCelda(f, c);

                if (celda.obtenerPiso().esDestino() && !celda.obtenerEntidad().esCaja()) {
                    return false;
                }
            }
        }
        return true;
    }
}