package tablero.background;

public class EspacioVacio implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "EV";
    }
}