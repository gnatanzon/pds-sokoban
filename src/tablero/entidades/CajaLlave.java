package tablero.entidades;

import sonido.GestorSonido;
import tablero.entidades.choque.ChoqueInerte;

public class CajaLlave extends Caja {

    public CajaLlave() {
        super(new ChoqueInerte());
    }


    @Override
    public String obtenerSimbolo() { return "CL"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA;
    }

    @Override
    public boolean puedeAbrirCerrojo() {
        return true;
    }

    @Override
    public boolean puedeCumplirObjetivo() {
        return false;
    }
}