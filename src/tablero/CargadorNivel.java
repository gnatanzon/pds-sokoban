package tablero;

import tablero.background.Piso;
import tablero.constructor.FabricaElementos;
import tablero.entidades.Entidad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargadorNivel {

    private final FabricaElementos fabrica;

    public CargadorNivel(FabricaElementos fabrica) {
        this.fabrica = fabrica;
    }

    public Tablero cargar(String rutaArchivo) throws IOException {
        List<String[]> lineas = leerLineas(rutaArchivo);
        int filas = lineas.size();
        int columnas = lineas.get(0).length;
        Celda[][] celdas = new Celda[filas][columnas];

        for (int f = 0; f < filas; f++) {
            String[] tokens = lineas.get(f);
            for (int c = 0; c < columnas; c++) {
                celdas[f][c] = parsearCelda(tokens[c]);
            }
        }

        return new Tablero(celdas);
    }

    private List<String[]> leerLineas(String rutaArchivo) throws IOException {
        List<String[]> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea.split(" "));
                }
            }
        }
        return lineas;
    }

    private Celda parsearCelda(String token) {
        String[] partes = token.split("-");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato inválido: " + token + ". Se esperaba 'piso-entidad'.");
        }
        int codigoPiso = Integer.parseInt(partes[0]);
        int codigoEntidad = Integer.parseInt(partes[1]);

        Piso piso = fabrica.crearPiso(codigoPiso);
        Entidad entidad = fabrica.crearEntidad(codigoEntidad);

        return new Celda(piso, entidad);
    }
}