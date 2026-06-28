package tablero.entidades.estado;

public class EstadoResistente implements EstadoFragil {

    private final int resistencia;

    public EstadoResistente(int resistencia) {
        this.resistencia = resistencia;
    }

    @Override
    public EstadoFragil recibirEmpuje() {
        int nueva = resistencia - 1;
        if (nueva <= 0) return new EstadoRoto();
        return new EstadoResistente(nueva);
    }

    @Override
    public boolean estaRota() { return false; }

    @Override
    public int obtenerResistencia() { return resistencia; }

    @Override
    public EstadoFragil copiar() {
        return new EstadoResistente(this.resistencia);
    }
}
