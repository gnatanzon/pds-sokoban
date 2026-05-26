package tablero.entidades;

public interface Entidad {
    boolean esSolida();
    String obtenerSimbolo();

    default boolean esCajaNormal() { return false; }
    default boolean esJugador()    { return false; }
}