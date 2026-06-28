package tablero.entidades.estado;

public interface EstadoFragil {
    EstadoFragil recibirEmpuje();
    boolean estaRota();
    int obtenerResistencia();
    EstadoFragil copiar();
}
