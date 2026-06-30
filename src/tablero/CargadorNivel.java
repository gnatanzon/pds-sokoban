package tablero;

import tablero.background.Piso;
import tablero.constructor.FabricaElementos;
import tablero.entidades.Entidad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargadorNivel {

    private final FabricaElementos fabrica;

    public CargadorNivel(FabricaElementos fabrica) {
        this.fabrica = fabrica;
    }

    public Tablero cargar(String rutaArchivo) throws IOException {
        List<String> lineas = leerLineas(rutaArchivo);
        List<String[]> lineasGrid = new ArrayList<>();
        int movMin = 0;

        for (String linea : lineas) {
            if (linea.contains("MovMin")) {
                // extrae dinámicamente el numero desp de 'MovMin='
                Matcher m = Pattern.compile("MovMin=(\\d+)").matcher(linea);
                if (m.find()) {
                    movMin = Integer.parseInt(m.group(1));
                }
            } else {
                // si es una línea del mapa
                lineasGrid.add(linea.split(" "));
            }
        }

        int filas = lineasGrid.size();
        int columnas = lineasGrid.get(0).length;
        Celda[][] celdas = new Celda[filas][columnas];

        for (int f = 0; f < filas; f++) {
            String[] tokens = lineasGrid.get(f);
            for (int c = 0; c < columnas; c++) {
                celdas[f][c] = parsearCelda(tokens[c]);
            }
        }

        return new Tablero(celdas, movMin);
    }

    private List<String> leerLineas(String rutaArchivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) { // sin "pds-sokoban/"
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
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