// tablero/entidades/EntidadConEstado.java
package tablero.entidades;

public interface EntidadConEstado<T> {
    T capturarEstado();
    void restaurarEstado(T estado);
}