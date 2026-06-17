package tablero.entidades;

import sonido.GestorSonido;

public class CajaFragil extends Caja {

    private static final int RESISTENCIA_INICIAL = 3;
    private int resistencia;

    public CajaFragil() {
        this.resistencia = RESISTENCIA_INICIAL;
    }

    public void recibirEmpuje() {
        if (resistencia > 0) resistencia--;
    }

    public boolean estaRota() { return resistencia == 0; }

    public int obtenerResistencia() { return resistencia; }

    @Override
    public String obtenerSimbolo() { return "CF"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CAJA_FRAGIL_ROTA;
    }
}