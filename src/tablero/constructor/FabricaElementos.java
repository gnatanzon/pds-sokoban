package tablero.constructor;

import tablero.background.Piso;
import tablero.entidades.Entidad;

public interface FabricaElementos {
    Piso crearPiso(int codigo);
    Entidad crearEntidad(int codigo);
}