package tablero.background;

public class MuroCerrado implements Piso {

    @Override
    public boolean esSolido() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        return "MC";
    }
}