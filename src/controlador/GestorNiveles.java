package controlador;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GestorNiveles {

    private final List<String> niveles;
    private int indiceActual;
    private int nivelDesbloqueado = 0; //Máximo nivel desbloqueado

    public GestorNiveles() {
        this.niveles = detectarNiveles();
        this.indiceActual = 0;
    }
    private List<String> detectarNiveles() {
        List<String> encontrados = new ArrayList<>();
        File directorio = new File("pds-sokoban");
        File[] archivos = directorio.listFiles((dir, nombre )-> nombre.matches("Nivel\\d+\\.txt"));

        if (archivos != null) {
            for (File archivo : archivos) {
                encontrados.add(archivo.getName());
            }
            encontrados.sort(Comparator.comparingInt(GestorNiveles::extraerNumero));
        }
        return encontrados;

    }

    private static int extraerNumero(String nombreArchivo) {
        String numero = nombreArchivo.replaceAll("[^0-9]","");
        return numero.isEmpty() ? 0 : Integer.parseInt(numero);
    }

    public List<String> obtenerNiveles() {
        return List.copyOf(this.niveles);
    }

    public String obtenerNivelActual () {
        return niveles.get(indiceActual);

    }
    public boolean hayNivelSiguiente() {
        return indiceActual < niveles.size() -1;
    }

    public String avanzarNivelSiguiente() {
        if (!hayNivelSiguiente()) {
            throw new IllegalStateException("No hay más niveles.");
        }
        indiceActual++;
        return obtenerNivelActual();
    }

    public void seleccionarNivel(String rutaNivel) {
        int indice = niveles.indexOf(rutaNivel);
        if (indice < 0) {
            throw new IllegalStateException("No se ha seleccionado nivel " + rutaNivel);
        }
        indiceActual = indice;
    }
    public int obtenerIndiceActual() {
        return indiceActual;
    }

    public int obtenerTotalNiveles() {
        return niveles.size();
    }

    public String obtenerNombreNivel(String rutaNivel) {
        String nombre = rutaNivel.replace(".txt", "");
        StringBuilder sb = new StringBuilder();
        for (char ch : nombre.toCharArray()) {
            if (Character.isUpperCase(ch) && !sb.isEmpty()) sb.append(' ');
            sb.append(ch);
        }
        return sb.toString();
    }

    public boolean estaDesbloqueado(String rutaNivel) {
        int indice = niveles.indexOf(rutaNivel);
        return indice <= nivelDesbloqueado;
    }

    public void desbloquearSiguiente() {
        if (nivelDesbloqueado < niveles.size() - 1) {
            nivelDesbloqueado++;
        }
    }

    public int obtenerNivelDesbloqueado() {
        return nivelDesbloqueado;
    }

}
