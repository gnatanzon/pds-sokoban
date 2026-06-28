package controlador.memento;

import tablero.Celda;
import tablero.Tablero;
import tablero.background.Piso;
import tablero.entidades.Entidad;
import tablero.entidades.EntidadConEstado;

import java.util.HashMap;
import java.util.Map;

public class EstadoTablero {

    private final Piso[][]     pisos;
    private final Entidad[][]  entidades;
    private final Map<EntidadConEstado<Object>, Object> estadosCapturados = new HashMap<>();

    @SuppressWarnings("unchecked")
    public EstadoTablero(Tablero tablero) {
        int filas = tablero.obtenerFilas();
        int cols  = tablero.obtenerColumnas();
        pisos     = new Piso[filas][cols];
        entidades = new Entidad[filas][cols];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                Celda celda = tablero.obtenerCelda(f, c);
                pisos[f][c]     = celda.obtenerPiso();
                entidades[f][c] = celda.obtenerEntidad();

                if (celda.obtenerEntidad() instanceof EntidadConEstado<?> e) {
                    estadosCapturados.put(
                            (EntidadConEstado<Object>) e,
                            e.capturarEstado()
                    );
                }
            }
        }
    }

    public void restaurarEstadosCapturados() {
        estadosCapturados.forEach(EntidadConEstado::restaurarEstado);
    }

    public Piso[][]    getPisos()      { return pisos; }
    public Entidad[][] getEntidades()  { return entidades; }
}