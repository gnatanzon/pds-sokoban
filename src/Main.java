import tablero.CargadorNivel;
import tablero.Tablero;
import tablero.constructor.FabricaElementosSokoban;

public class Main {
    public static void main(String[] args) throws Exception {
        FabricaElementosSokoban fabrica = new FabricaElementosSokoban();
        CargadorNivel cargador = new CargadorNivel(fabrica);
        Tablero tablero = cargador.cargar("nivel1.txt");
        tablero.imprimir();
    }
}