package tablero.background;

import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.entidades.Caja;
import tablero.entidades.movimiento.EstrategiaMovimiento;
import tablero.entidades.movimiento.MovimientoNormal;

public interface Piso {
    boolean esSolido();
    String obtenerSimbolo();
    GestorSonido.Sonido obtenerSonido();

    default boolean usaSonidoDelJugador() {
        return false;
    }

    default EstrategiaMovimiento obtenerEstrategiaMovimiento() {
        return new MovimientoNormal();
    }

    default boolean permiteDeslizamiento() {
        return false;
    }

    default boolean esDestino() {
        return false;
    }

    default boolean esCerrojo() {
        return false;
    }

    default void alRecibirCaja(Tablero tablero, Celda celda, Caja caja) {
    }

    default void alSalirCaja(Tablero tablero, Celda celda, Caja caja) {
    }

    default boolean debeAbrirse() {
        return false;
    }

    default Piso abrir() {
        return this;
    }

    default boolean esDestructible() {
        return false;
    }

    default Piso destruir() {
        return this;
    }

    default boolean debeCerrarse() {
        return false;
    }

    default Piso cerrar() {
        return this;
    }
}