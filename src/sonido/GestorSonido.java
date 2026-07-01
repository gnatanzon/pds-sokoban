package sonido;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorSonido {

    private static GestorSonido instancia;
    private float volumen = 1.0f;
    private boolean silenciado = false;
    private Clip clipActual;
    private int prioridadActual = -1;
    private volatile boolean sonando = false;   // NUEVO: reemplaza a isRunning()

    private final ExecutorService hiloAudio = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "hilo-audio");
        t.setDaemon(true);
        return t;
    });


    private static final String RUTA = "/sonidos/";

    public enum Sonido {
        PARED               ("Pared.wav", 1),
        ESPACIO_VACIO_1     ("EspacioVacio1.wav", 0),
        ESPACIO_VACIO_2     ("EspacioVacio2.wav", 0),
        ESPACIO_VACIO_3     ("EspacioVacio3.wav", 0),
        DESTINO             ("Destino.wav", 1),
        PISO_RESBALADIZO    ("PisoResbaladizo.wav", 0),
        CERROJO             ("Cerrojo.wav", 1),
        CAJA                ("Caja.wav", 1),
        CAJA_FRAGIL_ROTA    ("CajaFragilRota.wav", 1),
        EXPLOSION           ("Explosion.wav", 2),
        SPRAY_ACUATICO      ("SprayAcuatico.wav", 1),
        VICTORIA            ("VICTORIA.wav", 2);


        private final String archivo;
        private final int prioridad;

        Sonido(String archivo, int prioridad) {
            this.archivo = archivo;
            this.prioridad = prioridad;
        }

        public String getArchivo()  { return archivo; }
        public int    getPrioridad(){ return prioridad; }
    }

    private GestorSonido() {}

    public static GestorSonido obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    public boolean alternarSilencio() {
        silenciado = !silenciado;
        return silenciado;
    }

    public boolean estaSilenciado() {
        return silenciado;
    }

    public void establecerVolumen(float valor) {
        volumen = Math.max(0.0f, Math.min(1.0f, valor)); // clamp entre 0 y 1
    }

    public float obtenerVolumen() {
        return volumen;
    }

    public void reproducir(Sonido sonido) {
        if (silenciado) return;
        hiloAudio.submit(() -> reproducirInterno(sonido));
    }
    
    private void aplicarVolumen(Clip clip) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float minDb = control.getMinimum();
        float maxDb = control.getMaximum();

        // volumen 0.0 -> minDb (silencio real), volumen 1.0 -> 0dB (sin atenuar)
        float db;
        if (volumen <= 0.0001f) {
            db = minDb;
        } else {
            db = (float) (Math.log10(volumen) * 20.0);
            db = Math.max(minDb, Math.min(maxDb, db));
        }
        control.setValue(db);
    }

    private synchronized void reproducirInterno(Sonido sonido) {
        try {
            // NUEVO: usa el flag propio, no isRunning()
            if (sonando && prioridadActual > sonido.getPrioridad()) {
                return;
            }

            if (clipActual != null) {
                clipActual.stop();
                clipActual.close();
            }

            URL url = getClass().getResource(RUTA + sonido.getArchivo());
            if (url == null) {
                System.err.println("No se encontró " + sonido.getArchivo());
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            aplicarVolumen(clip);

            clip.addLineListener(evento -> {
                if (evento.getType() == LineEvent.Type.STOP && clip == clipActual) {
                    sonando = false;   // NUEVO: libera la prioridad cuando termina solo
                }
            });

            clipActual = clip;
            prioridadActual = sonido.getPrioridad();
            sonando = true;   // NUEVO: se marca ANTES de start(), sin esperar a isRunning()
            clip.start();
        } catch (Exception e) {
            System.err.println("Error c/ " + sonido.getArchivo() + ": " + e.getMessage());
        }
    }
}