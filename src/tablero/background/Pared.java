package tablero.background;

public class Pared implements Piso {

    @Override
    public boolean esSolido() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        return "PA";
    }
}