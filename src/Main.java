import tablero.CargadorNivel;
import tablero.Tablero;
import tablero.constructor.FabricaElementosSokoban;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String rutaNivel = "Nivel1.txt";

        FabricaElementosSokoban fabrica = new FabricaElementosSokoban();
        CargadorNivel cargador = new CargadorNivel(fabrica);
        Tablero tablero = cargador.cargar(rutaNivel);

        SwingUtilities.invokeLater(() -> new VentanaPrincipal(tablero, rutaNivel));
    }
}
