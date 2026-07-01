package tablero.entidades.decorator;

import sonido.GestorSonido;
import tablero.Celda;
import tablero.Tablero;
import tablero.entidades.Caja;
import tablero.entidades.choque.ChoqueInerte;
import tablero.entidades.movimiento.EstrategiaMovimiento;

/**
 * Clase base del patrón Decorator sobre Caja.
 * Delega todo al componente concreto envuelto (cajaDecorada).
 * Las subclases sobreescriben sólo el comportamiento que quieren modificar.
 */
public abstract class CajaDecorator extends Caja {

    protected final Caja cajaDecorada;

    public CajaDecorator(Caja cajaDecorada) {
        super(new ChoqueInerte());
        this.cajaDecorada = cajaDecorada;
    }

    @Override
    public void alChocarConObstaculo(Tablero tablero, int fila, int columna) {
        cajaDecorada.alChocarConObstaculo(tablero, fila, columna);
    }

    @Override
    public void alDetonarVecino(Tablero tablero, int fila, int columna) {
        cajaDecorada.alDetonarVecino(tablero, fila, columna);
    }

    // --- delegación total al componente base ---

    @Override
    public String obtenerSimbolo() {
        return cajaDecorada.obtenerSimbolo();
    }

    @Override
    public GestorSonido.Sonido obtenerSonido() {
        return cajaDecorada.obtenerSonido();
    }

    @Override
    public void despuesDeSerEmpujada(Tablero tablero, Celda celdaFinal) {
        cajaDecorada.despuesDeSerEmpujada(tablero, celdaFinal);
    }

    @Override
    public boolean puedeAbrirCerrojo() {
        return cajaDecorada.puedeAbrirCerrojo();
    }

    @Override
    public boolean puedeCumplirObjetivo() {
        return cajaDecorada.puedeCumplirObjetivo();
    }

    @Override
    public boolean estaRota() {
        return cajaDecorada.estaRota();
    }

    @Override
    public EstrategiaMovimiento obtenerEstrategiaPropia() {
        return cajaDecorada.obtenerEstrategiaPropia(); // por defecto, delega
    }
}