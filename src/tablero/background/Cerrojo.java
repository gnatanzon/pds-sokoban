package tablero.background;

public class Cerrojo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "CC";
    }
}