package controlador;

import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.background.Destino;
import tablero.entidades.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControladorJuego extends KeyAdapter {

    private final Tablero tablero;
    private final Runnable onMovimiento;
    private final Runnable onNivelSuperado;
    private final GestorSonido sonido = GestorSonido.obtenerInstancia();

    private Jugador jugador;
    private int jugadorFila;
    private int jugadorColumna;
    private boolean nivelTerminado = false;

    public ControladorJuego(Tablero tablero, Runnable onMovimiento, Runnable onNivelSuperado) {
        this.tablero = tablero;
        this.onMovimiento = onMovimiento;
        this.onNivelSuperado = onNivelSuperado;
        ubicarJugador();
    }

    private void ubicarJugador() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                if (tablero.obtenerCelda(f, c).obtenerEntidad() instanceof Jugador j) {
                    jugador       = j;
                    jugadorFila   = f;
                    jugadorColumna = c;
                    return;
                }
            }
        }
        throw new IllegalStateException("No se encontró al jugador en el tablero.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (nivelTerminado) return;

        int deltaFila    = 0;
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

        if (destino.obtenerPiso().esSolido()) {
            sonido.reproducir(destino.obtenerPiso().obtenerSonido());
            return;
        }

        if (destino.tieneEntidad()) {
            if (!(destino.obtenerEntidad() instanceof Caja)) return;
            if (!intentarEmpujarCaja(nuevaFila, nuevaColumna, deltaFila, deltaColumna)) return;
        }

        tablero.obtenerCelda(jugadorFila, jugadorColumna).establecerEntidad(new Nada());
        destino.establecerEntidad(jugador);

        jugadorFila    = nuevaFila;
        jugadorColumna = nuevaColumna;

        GestorSonido.Sonido sonidoAReproducir = destino.obtenerPiso().usaSonidoDelJugador()
                ? jugador.obtenerSonido()
                : destino.obtenerPiso().obtenerSonido();

        sonido.reproducir(sonidoAReproducir);

        onMovimiento.run();

        if (verificarNivelCompleto()) {
            nivelTerminado = true;
            onNivelSuperado.run();
        }
    }

    private boolean intentarEmpujarCaja(int cajaFila, int cajaColumna, int deltaFila, int deltaColumna) {
        int filaDestinoCaja    = cajaFila    + deltaFila;
        int columnaDestinoCaja = cajaColumna + deltaColumna;

        if (!tablero.dentroDelBorde(filaDestinoCaja, columnaDestinoCaja)) return false;

        Celda destinoCaja = tablero.obtenerCelda(filaDestinoCaja, columnaDestinoCaja);

        if (!destinoCaja.estaLibre()) return false;

        Entidad caja = tablero.obtenerCelda(cajaFila, cajaColumna).obtenerEntidad();
        sonido.reproducir(caja.obtenerSonido());

        destinoCaja.establecerEntidad(caja);
        tablero.obtenerCelda(cajaFila, cajaColumna).establecerEntidad(new Nada());

        return true;
    }

    private boolean verificarNivelCompleto() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                if (celda.obtenerPiso() instanceof Destino) {
                    if (!(celda.obtenerEntidad() instanceof Caja)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}