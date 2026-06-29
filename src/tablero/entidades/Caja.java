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

    public void despuesDeSerEmpujada(Tablero tablero, Celda celdaFinal) {
        // Por defecto no hace nada
    }

    public boolean puedeAbrirCerrojo() {
        return false;
    }
}