package tablero.background;

import sonido.GestorSonido;
import tablero.Tablero;
import tablero.Celda;
import tablero.entidades.Caja;

public class Cerrojo implements Piso {

    @Override
    public boolean esSolido() {
        return false;
    }

    @Override
    public String obtenerSimbolo() {
        return "CC";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.CERROJO;
    }

    @Override
    public boolean esCerrojo() {
        return true;
    }

    @Override
    public void alRecibirCaja(Tablero tablero, Celda celda, Caja caja) {
        if (caja.puedeAbrirCerrojo()) {
            GestorSonido.obtenerInstancia().reproducir(GestorSonido.Sonido.CERROJO); // NUEVO
        }
        tablero.actualizarEstadoMuros();
    }

    @Override
    public void alSalirCaja(Tablero tablero, Celda celda, Caja caja) {
        tablero.actualizarEstadoMuros();
    }
}