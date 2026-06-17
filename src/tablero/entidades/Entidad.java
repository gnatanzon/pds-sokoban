package tablero.entidades;

import sonido.GestorSonido;

public interface Entidad {
    boolean esSolida();
    String obtenerSimbolo();
    GestorSonido.Sonido obtenerSonido();
}