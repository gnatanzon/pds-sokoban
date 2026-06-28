package controlador;

import controlador.memento.HistorialMovimientos;
import controlador.observer.ObservadorJuego;
import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.background.*;
import tablero.entidades.*;
import tablero.entidades.movimiento.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorJuego extends KeyAdapter {

    private final Tablero tablero;
    private final GestorSonido sonido = GestorSonido.obtenerInstancia();
    private final HistorialMovimientos historial = new HistorialMovimientos();
    private final List<ObservadorJuego> observadores = new ArrayList<>();

    private Jugador jugador;
    private int jugadorFila;
    private int jugadorColumna;
    private boolean nivelTerminado = false;

    // Estrategias de movimiento
    private final EstrategiaMovimiento movimientoNormal      = new MovimientoNormal();
    private final EstrategiaMovimiento movimientoResbaladizo = new MovimientoResbaladizo();

    public ControladorJuego(Tablero tablero) {
        this.tablero = tablero;
        ubicarJugador();
    }

    public void agregarObservador(ObservadorJuego obs) {
        observadores.add(obs);
    }

    private void notificarMovimiento()   { observadores.forEach(ObservadorJuego::onMovimiento); }
    private void notificarNivelSuperado(){ observadores.forEach(ObservadorJuego::onNivelSuperado); }
    private void notificarUndo()         { observadores.forEach(ObservadorJuego::onUndo); }

    private void ubicarJugador() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                if (tablero.obtenerCelda(f, c).obtenerEntidad() instanceof Jugador j) {
                    jugador        = j;
                    jugadorFila    = f;
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

    public void deshacerMovimiento() {
        if (nivelTerminado) return;
        if (historial.deshacer(tablero)) {
            ubicarJugador(); // reubica al jugador desde el estado restaurado
            notificarUndo();
        }
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

        // Guardar estado ANTES de mover (para Memento)
        historial.guardar(tablero);

        tablero.obtenerCelda(jugadorFila, jugadorColumna).limpiarEntidad();
        destino.establecerEntidad(jugador);

        jugadorFila    = nuevaFila;
        jugadorColumna = nuevaColumna;

        GestorSonido.Sonido sonidoAReproducir = destino.obtenerPiso().usaSonidoDelJugador()
                ? jugador.obtenerSonido()
                : destino.obtenerPiso().obtenerSonido();
        sonido.reproducir(sonidoAReproducir);

        notificarMovimiento();

        if (verificarNivelCompleto()) {
            nivelTerminado = true;
            notificarNivelSuperado();
        }
    }

    private boolean intentarEmpujarCaja(int cajaFila, int cajaCol, int deltaFila, int deltaCol) {
        Celda celdaCaja = tablero.obtenerCelda(cajaFila, cajaCol);
        Entidad entidad = celdaCaja.obtenerEntidad();

        if (!(entidad instanceof Caja caja)) return false;

        // Elegir estrategia según el piso donde está la caja
        EstrategiaMovimiento estrategia = (celdaCaja.obtenerPiso() instanceof PisoResbaladizo)
                ? movimientoResbaladizo
                : movimientoNormal;

        ResultadoMovimiento resultado = estrategia.mover(tablero, cajaFila, cajaCol,
                deltaFila, deltaCol, caja);
        if (!resultado.isExito()) return false;

        historial.registrarEmpuje();

        sonido.reproducir(caja.obtenerSonido());

        // State: si es frágil, aplicar empuje y verificar rotura
        if (caja instanceof CajaFragil cajaFragil) {
            cajaFragil.recibirEmpuje();
            if (cajaFragil.estaRota()) {
                tablero.obtenerCelda(resultado.getFilaFinal(), resultado.getColFinal())
                        .limpiarEntidad();
            }
        }

        // CajaLlave: si cayó sobre un cerrojo, abrir muros correspondientes
        if (caja instanceof CajaLlave) {
            Celda celdaDestino = tablero.obtenerCelda(resultado.getFilaFinal(), resultado.getColFinal());
            if (celdaDestino.obtenerPiso() instanceof Cerrojo) {
                abrirMuros();
            }
        }

        return true;
    }

    /** Convierte todos los MuroCerrado en MuroAbierto */
    private void abrirMuros() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                if (celda.obtenerPiso() instanceof MuroCerrado) {
                    celda.establecerPiso(new MuroAbierto());
                }
            }
        }
    }

    private boolean verificarNivelCompleto() {
        for (int f = 0; f < tablero.obtenerFilas(); f++) {
            for (int c = 0; c < tablero.obtenerColumnas(); c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                if (celda.obtenerPiso() instanceof Destino) {
                    if (!(celda.obtenerEntidad() instanceof Caja)) return false;
                }
            }
        }
        return true;
    }

    public boolean puedeDeshacer() { return historial.puedeDeshacer(); }
    public int getUndosConsecutivos() { return historial.getUndosConsecutivos(); }
    public int getMaxUndosConsec()    { return historial.getMaxUndosConsec(); }

    public HistorialMovimientos getHistorial() {
        return historial;
    }
}