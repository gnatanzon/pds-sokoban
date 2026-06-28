package tablero.entidades;

import sonido.GestorSonido;
import tablero.entidades.estado.EstadoFragil;
import tablero.entidades.estado.EstadoResistente;

public class CajaFragil extends Caja implements EntidadConEstado<EstadoFragil> {

    private static final int RESISTENCIA_INICIAL = 3;
    private EstadoFragil estado;

    public CajaFragil() {
        this.estado = new EstadoResistente(RESISTENCIA_INICIAL);
    }

    public void recibirEmpuje() {
        estado = estado.recibirEmpuje();
    }

    public boolean estaRota() { return estado.estaRota(); }

    public int obtenerResistencia() { return estado.obtenerResistencia(); }

    @Override
    public String obtenerSimbolo() { return "CF"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA_FRAGIL_ROTA;
    }

    @Override
    public EstadoFragil capturarEstado() {
        return estado.copiar();
    }

    @Override
    public void restaurarEstado(EstadoFragil estadoGuardado) {
        this.estado = estadoGuardado;
    }
}