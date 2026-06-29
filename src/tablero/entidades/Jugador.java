package tablero.entidades;

import sonido.GestorSonido;

public class Jugador implements Entidad {

    private String nombre;
    private String spritePath;
    private GestorSonido.Sonido sonidoEspacioVacio;

    private Jugador() {}

    public String getNombre()     { return nombre; }
    public String getSpritePath() { return spritePath; }


    public GestorSonido.Sonido obtenerSonido() {
        return sonidoEspacioVacio;
    }

    @Override public boolean esJugador()        { return true; }

    @Override public Jugador comoJugador() {
        return this;
    }
    @Override public boolean esSolida()         { return true; }
    @Override public String obtenerSimbolo()    { return "JU"; }

    public static class Builder {

        private String nombre               = "Jugador";
        private String spritePath           = "jugador1.png";
        private GestorSonido.Sonido sonido  = GestorSonido.Sonido.ESPACIO_VACIO_1;

        public Builder nombre(String nombre) {
            if (nombre != null && !nombre.trim().isEmpty())
                this.nombre = nombre.trim();
            return this;
        }

        public Builder sprite(int opcion) {
            this.spritePath = (opcion >= 1 && opcion <= 3)
                    ? "jugador" + opcion + ".png"
                    : "jugador1.png";
            return this;
        }

        public Builder sonido(int opcion) {
            this.sonido = switch (opcion) {
                case 2  -> GestorSonido.Sonido.ESPACIO_VACIO_2;
                case 3  -> GestorSonido.Sonido.ESPACIO_VACIO_3;
                default -> GestorSonido.Sonido.ESPACIO_VACIO_1;
            };
            return this;
        }

        public Jugador build() {
            Jugador j = new Jugador();
            j.nombre             = this.nombre;
            j.spritePath         = this.spritePath;
            j.sonidoEspacioVacio = this.sonido;
            return j;
        }
    }
}