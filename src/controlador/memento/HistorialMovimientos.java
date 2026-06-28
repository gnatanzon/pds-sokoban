package controlador.memento;

import tablero.Celda;
import tablero.Tablero;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Caretaker del patrón Memento.
 * Guarda hasta MAX_HISTORIAL estados y permite restaurar de a PASOS_POR_UNDO.
 */
public class HistorialMovimientos {

    private static final int MAX_HISTORIAL    = 15;
    private static final int PASOS_POR_UNDO   = 5;
    private static final int MAX_UNDOS_CONSEC = 3;
    private int totalMovimientos = 0;
    private int totalEmpujes     = 0;
    private int totalUndos       = 0;

    private final Deque<EstadoTablero> historial = new ArrayDeque<>();
    private int undosConsecutivos = 0;

    /** Guarda el estado actual del tablero. Llamar ANTES de cada movimiento. */
    public void guardar(Tablero tablero) {
        if (historial.size() >= MAX_HISTORIAL) {
            historial.pollFirst(); // saca el más viejo
        }
        historial.addLast(new EstadoTablero(tablero));
        undosConsecutivos = 0; // cualquier movimiento real resetea el contador
        totalMovimientos++;
    }

    /**método  para empujes (llamarlo desde ControladorJuego):*/
    public void registrarEmpuje() { totalEmpujes++; }

    /** Devuelve true si se puede hacer undo ahora. */
    public boolean puedeDeshacer() {
        return !historial.isEmpty() && undosConsecutivos < MAX_UNDOS_CONSEC;
    }

    /**
     * Restaura el tablero PASOS_POR_UNDO movimientos atrás.
     * Devuelve true si se realizó el undo.
     */
    public boolean deshacer(Tablero tablero) {
        if (!puedeDeshacer()) return false;

        // Descartamos hasta PASOS_POR_UNDO estados del historial
        EstadoTablero estadoObjetivo = null;
        for (int i = 0; i < PASOS_POR_UNDO; i++) {
            if (!historial.isEmpty()) {
                estadoObjetivo = historial.pollLast();
            }
        }

        if (estadoObjetivo == null) return false;

        restaurar(tablero, estadoObjetivo);
        undosConsecutivos++;
        totalUndos++;
        return true;
    }

    private void restaurar(Tablero tablero, EstadoTablero estado) {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                celda.establecerPiso(estado.getPisos()[f][c]);
                celda.establecerEntidad(estado.getEntidades()[f][c]);
            }
        }
    }

    public int getUndosConsecutivos() { return undosConsecutivos; }
    public int getMaxUndosConsec()    { return MAX_UNDOS_CONSEC; }

    public int getTotalMovimientos() { return totalMovimientos; }
    public int getTotalEmpujes()     { return totalEmpujes; }
    public int getTotalUndos()       { return totalUndos; }
}