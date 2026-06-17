package tablero.background;

import sonido.GestorSonido;

public class PisoResbaladizo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "TR";
    }


    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.PISO_RESBALADIZO;
    }
}