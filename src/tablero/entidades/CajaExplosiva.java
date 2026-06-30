package tablero.entidades;

import sonido.GestorSonido;
import tablero.entidades.choque.ChoqueExplosivo;

public class CajaExplosiva extends Caja {

    public CajaExplosiva() {
        super(new ChoqueExplosivo());
    }

    @Override
    public String obtenerSimbolo() { return "CE"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA;
    }

    @Override
    public boolean puedeCumplirObjetivo() { return true; }
}
