package tablero.background;

import sonido.GestorSonido;

public class EspacioVacio implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "EV";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.ESPACIO_VACIO_1; // fallback, no se usa si usaSonidoDelJugador() es true
    }

    @Override
    public boolean usaSonidoDelJugador() {
        return true;
    }
}