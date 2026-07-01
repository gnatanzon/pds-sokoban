package tablero.entidades;

import tablero.Tablero;
import tablero.entidades.choque.ComportamientoDeChoque;

public abstract class Caja implements Entidad {

    private final ComportamientoDeChoque comportamientoDeChoque;

    protected Caja(ComportamientoDeChoque comportamientoDeChoque) {
        this.comportamientoDeChoque = comportamientoDeChoque;
    }

    @Override
    public boolean esSolida() { return true; }

    @Override
    public boolean esCaja() { return true; }

    @Override
    public Caja comoCaja() { return this; }

    public void despuesDeSerEmpujada(Tablero tablero, int fila, int columna) {
        comportamientoDeChoque.alSerEmpujada(tablero, fila, columna, this);
    }

    public void alChocarConObstaculo(Tablero tablero, int fila, int columna) {
        comportamientoDeChoque.alChocarConObstaculo(tablero, fila, columna, this);
    }

    public void alDetonarVecino(Tablero tablero, int fila, int columna) {
        comportamientoDeChoque.alDetonarVecino(tablero, fila, columna, this);
    }

    public void recibirEmpuje() {
    }

    /** Por defecto, una caja nunca "está rota". Solo CajaFragil lo redefine. */
    public boolean estaRota() {
        return false;
    }

    public boolean puedeAbrirCerrojo() {
        return false;
    }
}