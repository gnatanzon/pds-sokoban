package tablero.constructor;

import tablero.background.*;
import tablero.entidades.*;

public class FabricaElementosSokoban implements FabricaElementos {

    @Override
    public Piso crearPiso(int codigo) {
        switch (codigo) {
            case 0: return new Pared();
            case 1: return new EspacioVacio();
            case 2: return new Destino();
            case 3: return new PisoResbaladizo();
            case 4: return new Cerrojo();
            case 5: return new MuroCerrado();
            case 6: return new MuroAbierto();
            default: throw new IllegalArgumentException("Código de piso desconocido: " + codigo);
        }
    }

    @Override
    public Entidad crearEntidad(int codigo) {
        switch (codigo) {
            case 0: return new Nada();
            case 1: return new Jugador.Builder().build();
            case 2: return new CajaNormal();
            case 3: return new CajaFragil();
            case 4: return new CajaLlave();
            default: throw new IllegalArgumentException("Código de entidad desconocido: " + codigo);
        }
    }
}