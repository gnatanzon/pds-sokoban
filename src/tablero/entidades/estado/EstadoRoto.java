package tablero.entidades.estado;

public class EstadoRoto implements EstadoFragil{
    @Override
    public EstadoFragil recibirEmpuje() {
        return this; // ya rota, no cambia
    }

    @Override
    public boolean estaRota() { return true; }

    @Override
    public int obtenerResistencia() { return 0; }

    @Override
    public EstadoFragil copiar() {
        return new EstadoRoto(); // sin estado, instancia nueva alcanza
    }
}
