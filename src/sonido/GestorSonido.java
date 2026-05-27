package sonido;

import javax.sound.sampled.*;
import java.net.URL;

public class GestorSonido {

    private static GestorSonido instancia;

    private static final String RUTA = "/sonidos/";

    public enum Sonido {
        PARED           ("Pared.mp3"),
        ESPACIO_VACIO   ("EspacioVacio.mp3"),
        DESTINO         ("Destino.mp3"),
        PISO_RESBALADIZO("PisoResbaladizo.mp3"),
        CERROJO         ("Cerrojo.mp3"),
        CAJA            ("Caja.mp3"),
        CAJA_FRAGIL_ROTA("CajaFragilRota.mp3");

        private final String archivo;

        Sonido(String archivo) {
            this.archivo = archivo;
        }

        public String getArchivo() {
            return archivo;
        }
    }

    private GestorSonido() {}

    public static GestorSonido obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    public void reproducir(Sonido sonido) {
        try {
            URL url = getClass().getResource(RUTA + sonido.getArchivo());
            if (url == null) {
                System.err.println("No se encontró el sonido: " + sonido.getArchivo());
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir " + sonido.getArchivo() + ": " + e.getMessage());
        }
    }
}