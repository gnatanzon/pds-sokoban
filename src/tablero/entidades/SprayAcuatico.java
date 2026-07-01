package tablero.entidades;

import sonido.GestorSonido;

public class SprayAcuatico implements Entidad {

    @Override
    public boolean esSolida() {
        // No bloquea el movimiento del jugador: entra a la celda para recogerlo.
        return false;
    }

    @Override
    public boolean esPowerUp() {
        return true;
    }

    @Override
    public String obtenerSimbolo() {
        // Asegurate de registrar este símbolo en CargadorAssets con su sprite.
        return "SA";
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return GestorSonido.Sonido.SPRAY_ACUATICO;
    }
}
