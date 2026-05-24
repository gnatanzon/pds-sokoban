package tablero.entidades;

public class Nada implements Entidad {

    @Override
    public boolean esSolida() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "--";
    }
}