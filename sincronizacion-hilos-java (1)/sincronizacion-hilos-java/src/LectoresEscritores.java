/** Demostracion del problema Lectores-Escritores con un monitor. */
public class LectoresEscritores {
    public static void ejecutar() throws InterruptedException {
        final int numLectores = 3, lecturasC = 3;
        final int numEscritores = 2, escriturasC = 3;
        BaseDatosCompartida bd = new BaseDatosCompartida();

        System.out.println("=== LECTORES-ESCRITORES usando MONITOR (synchronized + wait/notifyAll) ===");
        Thread[] hilos = new Thread[numLectores + numEscritores];
        int k = 0;
        for (int i = 1; i <= numLectores; i++)  hilos[k++] = new Lector("Lector-" + i, bd, lecturasC);
        for (int i = 1; i <= numEscritores; i++) hilos[k++] = new Escritor("Escritor-" + i, bd, escriturasC);

        for (Thread t : hilos) t.start();
        for (Thread t : hilos) t.join();     // Join: Main espera a todos los hilos

        int esperadas = numEscritores * escriturasC;
        int valorFinal = bd.leerA();
        System.out.println("Lecturas realizadas: " + bd.getLecturas() + " (esperadas " + (numLectores * lecturasC) + ")");
        System.out.println("Escrituras realizadas: " + bd.getEscrituras() + " (esperadas " + esperadas + ")");
        System.out.println("Maximo de lectores simultaneos: " + bd.getMaxLectoresSimultaneos());
        System.out.println("Valor final del registro: " + valorFinal + " (esperado " + esperadas + ")");
        System.out.println("Lecturas inconsistentes detectadas: " + bd.getInconsistencias());
        if (valorFinal == esperadas && bd.getInconsistencias() == 0) {
            System.out.println(">>> El monitor garantizo exclusion mutua entre escritores y entre lectores y escritores.");
        } else {
            System.out.println(">>> ATENCION: se detecto una anomalia en la sincronizacion.");
        }
        System.out.println("=== FIN DE LECTORES-ESCRITORES ===");
    }
}
