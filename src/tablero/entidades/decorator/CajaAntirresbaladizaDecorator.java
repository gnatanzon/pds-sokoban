package tablero.entidades.decorator;

import tablero.entidades.Caja;
import tablero.entidades.movimiento.EstrategiaMovimiento;
import tablero.entidades.movimiento.MovimientoNormal;

public class CajaAntirresbaladizaDecorator extends CajaDecorator {

    public CajaAntirresbaladizaDecorator(Caja cajaDecorada) {
        super(cajaDecorada);
    }


    // anula la estrategia del piso: siempre devuelve MovimientoNormal, ignorando si el destino es resbaladizo.
    @Override
    public EstrategiaMovimiento obtenerEstrategiaPropia() {
        return new MovimientoNormal();
    }

    @Override
    public String obtenerSimbolo() {
        return "CA_SPRAY";
    }
}
