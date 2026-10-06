/** Demostracion del problema de los filosofos comensales con semaforos. */
public class CenaFilosofos {
    public static void ejecutar() throws InterruptedException {
        final int filosofos = 5;
        final int comidasPorFilosofo = 3;
        Mesa mesa = new Mesa(filosofos);

        System.out.println("=== CENA DE LOS FILOSOFOS usando SEMAFOROS (5 filosofos, 3 comidas c/u) ===");
        Filosofo[] hilos = new Filosofo[filosofos];
        for (int i = 0; i < filosofos; i++) {
            hilos[i] = new Filosofo(i, mesa, comidasPorFilosofo);
        }
        for (Filosofo f : hilos) f.start();
        for (Filosofo f : hilos) f.join();   // Join: Main espera a que todos terminen

        int esperadas = filosofos * comidasPorFilosofo;
        System.out.println("Comidas esperadas: " + esperadas);
        System.out.println("Comidas realizadas: " + mesa.getComidasTotales());
        System.out.println("Violaciones de exclusion mutua (vecinos comiendo a la vez): " + mesa.getViolaciones());
        if (mesa.getComidasTotales() == esperadas && mesa.getViolaciones() == 0) {
            System.out.println(">>> Todos cenaron sin interbloqueo y sin conflictos por los tenedores.");
        } else {
            System.out.println(">>> ATENCION: se detecto una anomalia en la sincronizacion.");
        }
        System.out.println("=== FIN DE LA CENA ===");
    }
}
