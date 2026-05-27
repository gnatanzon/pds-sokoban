package tablero.entidades;

public class Jugador implements Entidad {

    public boolean esJugador() { return true; }

    @Override
    public boolean esSolida() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        return "JU";
    }
}