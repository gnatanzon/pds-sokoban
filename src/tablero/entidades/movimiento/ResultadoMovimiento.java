package tablero.entidades.movimiento;

public class ResultadoMovimiento {
    private final int filaFinal;
    private final int colFinal;
    private final boolean exito;

    public ResultadoMovimiento(int filaFinal, int colFinal, boolean exito) {
        this.filaFinal = filaFinal;
        this.colFinal  = colFinal;
        this.exito     = exito;
    }

    public int getFilaFinal()  { return filaFinal; }
    public int getColFinal()   { return colFinal; }
    public boolean isExito()   { return exito; }
}