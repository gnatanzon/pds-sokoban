package controlador.memento;

public class CalculadorPuntaje {

    private static final int BASE         = 1000;
    private static final int COSTO_MOV    = 2;
    private static final int COSTO_EMPUJE = 1;
    private static final int COSTO_UNDO   = 50;

    public static int calcular(int movimientos, int empujes, int undosUsados) {
        int puntaje = BASE - (movimientos * COSTO_MOV) - (empujes * COSTO_EMPUJE) - (undosUsados * COSTO_UNDO);
        return Math.max(puntaje, 0);
    }
}