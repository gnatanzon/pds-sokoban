package sonido;

import javax.sound.sampled.*;
import java.net.URL;

public class GestorSonido {

    private static GestorSonido instancia;

    private static final String RUTA = "/sonidos/";

    public enum Sonido {
        PARED               ("Pared.wav"),
        ESPACIO_VACIO_1     ("EspacioVacio1.wav"),
        ESPACIO_VACIO_2     ("EspacioVacio2.wav"),
        ESPACIO_VACIO_3     ("EspacioVacio3.wav"),
        DESTINO             ("Destino.wav"),
        PISO_RESBALADIZO    ("PisoResbaladizo.wav"),
        CERROJO             ("Cerrojo.wav"),
        CAJA                ("Caja.wav"),
        CAJA_FRAGIL_ROTA    ("CajaFragilRota.wav");


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
                System.err.println("No se encontró " + sonido.getArchivo());
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.addLineListener(evento -> {
                if (evento.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("Error c/ " + sonido.getArchivo() + ": " + e.getMessage());
        }
    }
}