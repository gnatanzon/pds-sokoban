package tablero.entidades;

import sonido.GestorSonido;

public class CajaNormal extends Caja {

    public boolean esCajaNormal() { return true; }

    @Override
    public String obtenerSimbolo() { return "CN"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA;
    }
}