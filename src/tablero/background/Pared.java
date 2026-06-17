package tablero.background;

import sonido.GestorSonido;

public class Pared implements Piso {

    @Override
    public boolean esSolido() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        return "PA";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.PARED;
    }
}