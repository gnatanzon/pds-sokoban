package controlador.memento;

import tablero.Celda;
import tablero.Tablero;

import java.util.ArrayDeque;
import java.util.Deque;

public class HistorialMovimientos {

    private static final int MAX_HISTORIAL    = 15;
    private static final int PASOS_POR_UNDO   = 5;
    private static final int MAX_UNDOS_CONSEC = 3;

    private int totalMovimientos = 0;
    private int totalEmpujes     = 0;
    private int totalUndos       = 0;

    private final Deque<EstadoTablero> historial = new ArrayDeque<>();
    private int undosConsecutivos = 0;

    public void guardar(Tablero tablero) {
        if (historial.size() >= MAX_HISTORIAL) {
            historial.pollFirst();
        }
        historial.addLast(new EstadoTablero(tablero));
        undosConsecutivos = 0;
        totalMovimientos++;
    }

    public void registrarEmpuje() { totalEmpujes++; }

    public boolean puedeDeshacer() {
        return !historial.isEmpty() && undosConsecutivos < MAX_UNDOS_CONSEC;
    }

    public boolean deshacer(Tablero tablero) {
        if (!puedeDeshacer()) return false;

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
        estado.restaurarEstadosCapturados();
    }

    public int getUndosConsecutivos() { return undosConsecutivos; }
    public int getMaxUndosConsec()    { return MAX_UNDOS_CONSEC; }
    public int getTotalMovimientos()  { return totalMovimientos; }
    public int getTotalEmpujes()      { return totalEmpujes; }
    public int getTotalUndos()        { return totalUndos; }
}