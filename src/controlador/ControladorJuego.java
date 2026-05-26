package controlador;

import tablero.Celda;
import tablero.Tablero;
import tablero.entidades.CajaNormal;
import tablero.entidades.Nada;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControladorJuego extends KeyAdapter {

    private final Tablero tablero;
    private final Runnable onMovimiento;

    private int jugadorFila;
    private int jugadorColumna;

    public ControladorJuego(Tablero tablero, Runnable onMovimiento) {
        this.tablero = tablero;
        this.onMovimiento = onMovimiento;
        ubicarJugador();
    }

    private void ubicarJugador() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                if (tablero.obtenerCelda(f, c).obtenerEntidad() instanceof tablero.entidades.Jugador) {
                    jugadorFila = f;
                    jugadorColumna = c;
                    return;
                }
            }
        }
        throw new IllegalStateException("No se encontró al jugador en el tablero.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int deltaFila = 0;
        int deltaColumna = 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W -> deltaFila    = -1;
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> deltaFila    =  1;
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> deltaColumna = -1;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> deltaColumna =  1;
            default -> { return; }
        }

        moverJugador(deltaFila, deltaColumna);
    }

    private void moverJugador(int deltaFila, int deltaColumna) {
        int nuevaFila    = jugadorFila    + deltaFila;
        int nuevaColumna = jugadorColumna + deltaColumna;

        if (!tablero.dentroDelBorde(nuevaFila, nuevaColumna)) return;

        Celda destino = tablero.obtenerCelda(nuevaFila, nuevaColumna);

        if (destino.obtenerPiso().esSolido()) return; // pared

        if (destino.tieneEntidad()) {
            // Solo empuja CajaNormal
            if (!(destino.obtenerEntidad() instanceof CajaNormal)) return;

            if (!intentarEmpujarCaja(nuevaFila, nuevaColumna, deltaFila, deltaColumna)) return;
        }

        // Mover jugador
        Celda celdaActual = tablero.obtenerCelda(jugadorFila, jugadorColumna);
        celdaActual.establecerEntidad(new Nada());
        destino.establecerEntidad(new tablero.entidades.Jugador());

        jugadorFila    = nuevaFila;
        jugadorColumna = nuevaColumna;

        onMovimiento.run();
    }

    private boolean intentarEmpujarCaja(int cajaFila, int cajaColumna, int deltaFila, int deltaColumna) {
        int filaDestinoCaja    = cajaFila    + deltaFila;
        int columnaDestinoCaja = cajaColumna + deltaColumna;

        if (!tablero.dentroDelBorde(filaDestinoCaja, columnaDestinoCaja)) return false;

        Celda destinoCaja = tablero.obtenerCelda(filaDestinoCaja, columnaDestinoCaja);

        if (!destinoCaja.estaLibre()) return false;

        // Mover la caja
        destinoCaja.establecerEntidad(tablero.obtenerCelda(cajaFila, cajaColumna).obtenerEntidad());
        tablero.obtenerCelda(cajaFila, cajaColumna).establecerEntidad(new Nada());

        return true;
    }
}
