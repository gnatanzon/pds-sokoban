package sonido;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorSonido {

    private static GestorSonido instancia;

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
        VICTORIA            ("Victoria.wav", 2),
        UNDO ("Undo.wav", 1);

        private final String archivo;
        private final int prioridad;

        Sonido(String archivo, int prioridad) {
            this.archivo = archivo;
            this.prioridad = prioridad;
        }

        public String getArchivo()  { return archivo; }
        public int    getPrioridad(){ return prioridad; }
    }

    private boolean silenciado = false;
    private float volumen = 1.0f;

    private Clip clipActual;
    private int  prioridadActual = -1;
    private volatile boolean sonando = false;

    // NUEVO: clips precargados y abiertos una sola vez
    private final Map<Sonido, Clip> clips = new EnumMap<>(Sonido.class);

    private final ExecutorService hiloAudio = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "hilo-audio");
        t.setDaemon(true);
        return t;
    });

    private GestorSonido() {
        precargarSonidos(); // NUEVO
    }

    public static GestorSonido obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    // NUEVO: carga y abre todos los clips una única vez, al arrancar
    private void precargarSonidos() {
        for (Sonido sonido : Sonido.values()) {
            try {
                URL url = getClass().getResource(RUTA + sonido.getArchivo());
                if (url == null) {
                    System.err.println("No se encontró " + sonido.getArchivo());
                    continue;
                }
                AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);

                clip.addLineListener(evento -> {
                    if (evento.getType() == LineEvent.Type.STOP && clip == clipActual) {
                        sonando = false;
                    }
                });

                clips.put(sonido, clip);
            } catch (Exception e) {
                System.err.println("Error al precargar " + sonido.getArchivo() + ": " + e.getMessage());
            }
        }
    }

    public void alternarSilencio() {
        silenciado = !silenciado;
    }

    public boolean estaSilenciado() {
        return silenciado;
    }

    public void establecerVolumen(float valor) {
        volumen = Math.max(0.0f, Math.min(1.0f, valor));
    }

    public float obtenerVolumen() {
        return volumen;
    }

    public void reproducir(Sonido sonido) {
        if (silenciado) return;
        hiloAudio.submit(() -> reproducirInterno(sonido));
    }

    private synchronized void reproducirInterno(Sonido sonido) {
        if (sonando && prioridadActual > sonido.getPrioridad()) {
            return;
        }

        Clip clip = clips.get(sonido);
        if (clip == null) {
            System.err.println("Sonido no disponible: " + sonido.getArchivo());
            return;
        }

        if (clipActual != null && clipActual.isRunning()) {
            clipActual.stop();
        }

        aplicarVolumen(clip);
        clip.setFramePosition(0); // rebobina en vez de reabrir

        clipActual = clip;
        prioridadActual = sonido.getPrioridad();
        sonando = true;
        clip.start();
    }

    private void aplicarVolumen(Clip clip) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float minDb = control.getMinimum();
        float maxDb = control.getMaximum();

        float db;
        if (volumen <= 0.0001f) {
            db = minDb;
        } else {
            db = (float) (Math.log10(volumen) * 20.0);
            db = Math.max(minDb, Math.min(maxDb, db));
        }
        control.setValue(db);
    }
}