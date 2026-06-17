package tablero.background;

import sonido.GestorSonido;

public class Cerrojo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "CC";
    }


    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CERROJO;
    }
}