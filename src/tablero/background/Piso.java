package tablero.background;

public interface Piso {
    boolean esSolido();
    String obtenerSimbolo();

    default boolean esDestino() { return false; }
}