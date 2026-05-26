package tablero.background;

public class Destino implements Piso {

    @Override
    public boolean esDestino() { return true; }

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "CD";
    }
}