package controlador.observer;

public interface ObservadorJuego {
    void onMovimiento();
    void onNivelSuperado();
    void onUndo();
}
