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

    default void alRecibirCaja(Tablero tablero, Celda celda, Caja caja) {
        // Por defecto no hace nada
    }

    default boolean debeAbrirse() {
        return false;
    }

    default Piso abrir() {
        return this;
    }
}