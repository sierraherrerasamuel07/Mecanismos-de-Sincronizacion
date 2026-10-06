import java.util.concurrent.atomic.AtomicInteger;

/**
 * Monitor del problema Lectores-Escritores. Varios lectores pueden leer a la vez;
 * un escritor necesita acceso exclusivo. Se da prioridad a los escritores en espera
 * para evitar su inanicion.
 * El registro tiene dos campos (a y b) que un escritor actualiza en dos pasos: si un
 * lector viera a != b significaria que leyo en medio de una escritura.
 */
public class BaseDatosCompartida {
    private volatile int a = 0;
    private volatile int b = 0;

    private int lectoresActivos = 0;
    private int escritoresEsperando = 0;
    private boolean escribiendo = false;

    private int maxLectoresSimultaneos = 0;
    private int lecturas = 0;
    private int escrituras = 0;
    private final AtomicInteger inconsistencias = new AtomicInteger();

    // ---------- protocolo de los lectores ----------
    public synchronized void iniciarLectura() throws InterruptedException {
        while (escribiendo || escritoresEsperando > 0) {
            wait();
        }
        lectoresActivos++;
        maxLectoresSimultaneos = Math.max(maxLectoresSimultaneos, lectoresActivos);
        System.out.println("[Monitor] " + Thread.currentThread().getName()
                + " entra a leer (lectores activos=" + lectoresActivos + ")");
    }

    public synchronized void terminarLectura() {
        lectoresActivos--;
        lecturas++;
        System.out.println("[Monitor] " + Thread.currentThread().getName()
                + " termina de leer (lectores activos=" + lectoresActivos + ")");
        if (lectoresActivos == 0) {
            notifyAll();
        }
    }

    // ---------- protocolo de los escritores ----------
    public synchronized void iniciarEscritura() throws InterruptedException {
        escritoresEsperando++;
        while (escribiendo || lectoresActivos > 0) {
            wait();
        }
        escritoresEsperando--;
        escribiendo = true;
        System.out.println("[Monitor] " + Thread.currentThread().getName() + " entra a ESCRIBIR (acceso exclusivo)");
    }

    public synchronized void terminarEscritura() {
        escribiendo = false;
        escrituras++;
        System.out.println("[Monitor] " + Thread.currentThread().getName() + " termina de escribir");
        notifyAll();
    }

    // ---------- acceso a los datos (fuera del monitor, protegido por el protocolo) ----------
    public int leerA() { return a; }
    public int leerB() { return b; }
    public void escribirA(int valor) { a = valor; }
    public void escribirB(int valor) { b = valor; }
    public void registrarInconsistencia() { inconsistencias.incrementAndGet(); }

    public synchronized int getLecturas()               { return lecturas; }
    public synchronized int getEscrituras()             { return escrituras; }
    public synchronized int getMaxLectoresSimultaneos() { return maxLectoresSimultaneos; }
    public int getInconsistencias()                     { return inconsistencias.get(); }
}
