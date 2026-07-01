package controlador;

import controlador.memento.HistorialMovimientos;
import controlador.observer.ObservadorJuego;
import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.background.Piso;
import controlador.memento.CalculadorPuntaje;

import tablero.entidades.*;
import tablero.entidades.decorator.CajaAntirresbaladizaDecorator;
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
                Entidad entidad = tablero.obtenerCelda(f, c).obtenerEntidad();

                if (entidad.esJugador()) {
                    jugador        = entidad.comoJugador();
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
            sonido.reproducir(GestorSonido.Sonido.UNDO);   // NUEVO
            ubicarJugador();
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
            Entidad entidadDestino = destino.obtenerEntidad();

            if (entidadDestino.esPowerUp()) {
                recogerPowerUp(entidadDestino);
                destino.limpiarEntidad();
            } else if (!entidadDestino.esCaja()) {
                return;
            } else if (!intentarEmpujarCaja(nuevaFila, nuevaColumna, deltaFila, deltaColumna)) {
                notificarMovimiento();
                return;
            }
        }

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
            sonido.reproducir(GestorSonido.Sonido.VICTORIA);   // NUEVO
            notificarNivelSuperado();
        }
    }

    private void recogerPowerUp(Entidad powerUp) {
        if (powerUp instanceof SprayAcuatico) {
            jugador.agregarCargaSpray();
            sonido.reproducir(powerUp.obtenerSonido());
        }
    }

    private boolean intentarEmpujarCaja(int cajaFila, int cajaCol, int deltaFila, int deltaCol) {
        Celda celdaCaja    = tablero.obtenerCelda(cajaFila, cajaCol);
        Celda celdaDestino = tablero.obtenerCelda(cajaFila + deltaFila, cajaCol + deltaCol);
        Entidad entidad    = celdaCaja.obtenerEntidad();

        if (!entidad.esCaja()) return false;

        Caja caja = entidad.comoCaja();

        //si el jugador tiene carga de spray, esta caja queda "sellada"
        if (jugador.tieneCargaSpray()) {
            caja = new CajaAntirresbaladizaDecorator(caja);
            celdaCaja.establecerEntidad(caja);   // reemplaza la caja en el tablero por la decorada
            jugador.consumirCargaSpray();
        }

        EstrategiaMovimiento estrategia = caja.obtenerEstrategiaPropia();
        if (estrategia == null) {
            estrategia = celdaDestino.obtenerPiso().obtenerEstrategiaMovimiento();
        }

        ResultadoMovimiento resultado = estrategia.mover(
                tablero, cajaFila, cajaCol, deltaFila, deltaCol, caja
        );

        if (!resultado.isExito()) {
            if (celdaDestino.obtenerPiso().esSolido()) {
                caja.alChocarConObstaculo(tablero, cajaFila, cajaCol);
            }
            return false;
        }

        historial.registrarEmpuje();
        sonido.reproducir(caja.obtenerSonido());

        Celda celdaFinal = tablero.obtenerCelda(resultado.getFilaFinal(), resultado.getColFinal());

        caja.despuesDeSerEmpujada(tablero, resultado.getFilaFinal(), resultado.getColFinal());
        celdaFinal.obtenerPiso().alRecibirCaja(tablero, celdaFinal, caja);

        return true;
    }

    private boolean verificarNivelCompleto() {
        return tablero.todosLosDestinosTienenCaja();
    }

    public boolean puedeDeshacer() { return historial.puedeDeshacer(); }
    public int getUndosConsecutivos() { return historial.getUndosConsecutivos(); }
    public int getMaxUndosConsec()    { return historial.getMaxUndosConsec(); }

    public HistorialMovimientos getHistorial() {
        return historial;
    }

    public int obtenerFilas()     { return tablero.obtenerFilas(); }
    public int obtenerColumnas()  { return tablero.obtenerColumnas(); }

    public int calcularPuntaje() {
        return CalculadorPuntaje.calcular(
                historial.getTotalMovimientos(),
                historial.getTotalEmpujes(),
                historial.getTotalUndos(),
                tablero.obtenerMovMin()
        );
    }
    public int obtenerMovimientosMinimos() { return tablero.obtenerMovMin(); }
}