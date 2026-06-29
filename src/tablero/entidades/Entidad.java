package tablero.entidades;

import sonido.GestorSonido;

public interface Entidad {
    boolean esSolida();
    String obtenerSimbolo();
    GestorSonido.Sonido obtenerSonido();

    default boolean esJugador() { return false; }

    default Jugador comoJugador() {
        throw new IllegalStateException("La entidad no es un jugador");
    }

    default boolean esCaja() { return false; }

    default Caja comoCaja() {
        throw new IllegalStateException("La entidad no es una caja");
    }
}