package tablero;

import tablero.background.Piso;
import tablero.entidades.Entidad;
import tablero.entidades.Nada;

public class Celda {

    private Piso piso;
    private Entidad entidad;

    public Celda(Piso piso, Entidad entidad) {
        this.piso = piso;
        this.entidad = entidad;
    }

    public Piso obtenerPiso() {
        return piso;
    }

    public void establecerPiso(Piso piso) {
        this.piso = piso;
    }

    public Entidad obtenerEntidad() {
        return entidad;
    }

    public void establecerEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public boolean tieneEntidad() {
        return !(entidad instanceof Nada);
    }

    public boolean estaLibre() {
        return !piso.esSolido() && !tieneEntidad();
    }

    @Override
    public String toString() {
        return piso.obtenerSimbolo() + "/" + entidad.obtenerSimbolo();
    }
}