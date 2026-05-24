package tablero.entidades;

public abstract class Caja implements Entidad {

    @Override
    public boolean esSolida() {
        return true;
    }
}