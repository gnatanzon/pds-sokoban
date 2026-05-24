package tablero.background;

public class MuroAbierto implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "MA";
    }
}