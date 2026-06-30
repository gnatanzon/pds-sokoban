package controlador.memento;

public class CalculadorPuntaje {

    private static final int PUNTAJE_MAXIMO  = 100;
    private static final int COSTO_MOV_EXTRA = 5;   // por cada movimiento sobre el mínimo
    private static final int COSTO_EMPUJE    = 2;   // por cada empuje de caja
    private static final int COSTO_UNDO      = 10;  // penalización por uso de undo

    public static int calcular(int movimientos, int empujes,
                               int undosUsados, int movimientosMinimos) {
        int excesoMovimientos = (movimientosMinimos > 0)
                ? Math.max(0, movimientos - movimientosMinimos)
                : 0;

        int puntaje = PUNTAJE_MAXIMO
                - excesoMovimientos * COSTO_MOV_EXTRA
                - undosUsados       * COSTO_UNDO;

        return Math.max(0, puntaje);
    }

    public static int getPuntajeMaximo() {
        return PUNTAJE_MAXIMO;
    }
}