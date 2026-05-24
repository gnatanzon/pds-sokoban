package tablero.background;

public class PisoResbaladizo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "TR";
    }
}