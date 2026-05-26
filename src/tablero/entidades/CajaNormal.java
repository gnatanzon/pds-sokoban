package tablero.entidades;

public class CajaNormal extends Caja {

    @Override
    public boolean esCajaNormal() { return true; }

    @Override
    public String obtenerSimbolo() {
        return "CN";
    }
}