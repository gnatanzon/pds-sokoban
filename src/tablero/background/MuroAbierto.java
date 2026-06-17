package tablero.background;

import sonido.GestorSonido;

public class MuroAbierto implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "MA";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.ESPACIO_VACIO_1;
    }

    @Override
    public boolean usaSonidoDelJugador() {
        return true;
    }
}