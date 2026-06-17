package tablero.entidades;

import sonido.GestorSonido;

public class Nada implements Entidad {

    @Override
    public boolean esSolida() { return false; }

    @Override
    public String obtenerSimbolo() { return "--"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return null; // Nada no emite sonido
    }
}