package tablero.entidades;

import tablero.Celda;
import tablero.Tablero;

public abstract class Caja implements Entidad {

    @Override
    public boolean esSolida() {
        return true;
    }

    @Override
    public boolean esCaja() {
        return true;
    }

    @Override
    public Caja comoCaja() {
        return this;
    }

    @Override
    public boolean puedeCumplirObjetivo() {
        return true;
    }

    public void despuesDeSerEmpujada(Tablero tablero, Celda celdaFinal) {
    }

    public boolean puedeAbrirCerrojo() {
        return false;
    }
}