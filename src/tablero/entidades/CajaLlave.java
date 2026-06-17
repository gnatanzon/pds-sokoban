package tablero.entidades;

import sonido.GestorSonido;

public class CajaLlave extends Caja {

    @Override
    public String obtenerSimbolo() { return "CL"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA;
    }
}