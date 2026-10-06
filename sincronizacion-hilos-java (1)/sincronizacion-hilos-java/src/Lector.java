import java.util.concurrent.ThreadLocalRandom;

/** Hilo lector: lee los dos campos del registro y verifica que sean consistentes. */
public class Lector extends Thread {
    private final BaseDatosCompartida bd;
    private final int lecturas;

    public Lector(String nombre, BaseDatosCompartida bd, int lecturas) {
        super(nombre);
        this.bd = bd;
        this.lecturas = lecturas;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < lecturas; i++) {
                bd.iniciarLectura();
                try {
                    int x = bd.leerA();
                    Thread.sleep(40);                 // tiempo de lectura
                    int y = bd.leerB();
                    if (x != y) bd.registrarInconsistencia();
                } finally {
                    bd.terminarLectura();
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(20, 80));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
