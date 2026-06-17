package tablero.background;

import sonido.GestorSonido;

public interface Piso {
    boolean esSolido();
    String obtenerSimbolo();

    GestorSonido.Sonido obtenerSonido();

    default boolean usaSonidoDelJugador() {
        return false;
    }
}