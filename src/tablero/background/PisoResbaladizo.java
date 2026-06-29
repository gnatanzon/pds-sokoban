package tablero.background;

import sonido.GestorSonido;
import tablero.entidades.movimiento.EstrategiaMovimiento;
import tablero.entidades.movimiento.MovimientoResbaladizo;

public class PisoResbaladizo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "TR";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.PISO_RESBALADIZO;
    }

    @Override
    public EstrategiaMovimiento obtenerEstrategiaMovimiento() {
        return new MovimientoResbaladizo();
    }

    @Override
    public boolean permiteDeslizamiento() {
        return true;
    }
}