package tablero.background;

import sonido.GestorSonido;

public class MuroCerrado implements Piso {

    @Override
    public boolean esSolido() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        return "MC";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.PARED;
    }
    @Override
    public boolean debeAbrirse() {
        return true;
    }

    @Override
    public Piso abrir() {
        return new MuroAbierto();
    }
}