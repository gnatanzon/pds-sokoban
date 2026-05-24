import tablero.CargadorNivel;
import tablero.Tablero;
import tablero.constructor.FabricaElementosSokoban;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        FabricaElementosSokoban fabrica = new FabricaElementosSokoban();
        CargadorNivel cargador = new CargadorNivel(fabrica);
        Tablero tablero = cargador.cargar("nivel1.txt");

        SwingUtilities.invokeLater(() -> new VentanaPrincipal(tablero));
    }
}