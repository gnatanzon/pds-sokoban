package tablero.background;

import sonido.GestorSonido;

/** Muro interior que puede ser destruido por una caja explosiva. NUNCA usar en el borde. */
public class ParedDestructible implements Piso {

    @Override
    public boolean esSolido() { return true; }

    @Override
    public String obtenerSimbolo() { return "PD"; }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.PARED;
    }

    @Override
    public boolean esDestructible() { return true; }

    @Override
    public Piso destruir() { return new MuroAbierto(); }
}
