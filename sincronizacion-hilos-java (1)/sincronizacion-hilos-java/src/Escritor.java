import java.util.concurrent.ThreadLocalRandom;

/** Hilo escritor: incrementa el registro en dos pasos (a y luego b). */
public class Escritor extends Thread {
    private final BaseDatosCompartida bd;
    private final int escrituras;

    public Escritor(String nombre, BaseDatosCompartida bd, int escrituras) {
        super(nombre);
        this.bd = bd;
        this.escrituras = escrituras;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < escrituras; i++) {
                bd.iniciarEscritura();
                try {
                    int nuevo = bd.leerA() + 1;
                    bd.escribirA(nuevo);
                    Thread.sleep(40);                 // ventana en la que a != b
                    bd.escribirB(nuevo);
                } finally {
                    bd.terminarEscritura();
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(30, 100));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
