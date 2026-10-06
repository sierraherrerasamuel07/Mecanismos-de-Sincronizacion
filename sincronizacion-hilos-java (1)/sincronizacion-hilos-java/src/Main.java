import java.util.Scanner;

/** Punto de entrada: menu interactivo de consola con las seis demostraciones. */
public class Main {

    private static void ejecutarCuenta(CuentaBancaria cuenta, String etiqueta) throws InterruptedException {
        final int clientes = 4;
        final int depositosPorCliente = 1_000_000;
        final int monto = 1;

        System.out.println("=== CUENTA BANCARIA COMPARTIDA " + etiqueta + " ===");
        System.out.println("Clientes: " + clientes + " | Depositos por cliente: " + depositosPorCliente + " | Monto: " + monto);

        Cliente[] hilos = new Cliente[clientes];
        for (int i = 0; i < clientes; i++) {
            hilos[i] = new Cliente("Cliente-" + (i + 1), cuenta, depositosPorCliente, monto);
        }
        long inicio = System.nanoTime();
        for (Cliente c : hilos) c.start();
        for (Cliente c : hilos) c.join();    // Join: se espera a que todos terminen
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        int esperado = clientes * depositosPorCliente * monto;
        int obtenido = cuenta.obtenerSaldo();
        System.out.println("Saldo esperado (sin condiciones de carrera): " + esperado);
        System.out.println("Saldo obtenido:                              " + obtenido);
        if (esperado == obtenido) {
            System.out.println(">>> Coinciden: no se perdio ningun deposito.");
        } else {
            System.out.println(">>> NO coinciden: se perdieron " + (esperado - obtenido) + " depositos por condicion de carrera.");
        }
        System.out.println("Tiempo de ejecucion: " + ms + " ms");
        System.out.println("=== FIN DE LA DEMOSTRACION ===");
    }

    private static boolean ejecutarOpcion(int opcion) throws InterruptedException {
        switch (opcion) {
            case 1 -> ejecutarCuenta(new CuentaSinSincronizar(), "SIN SINCRONIZACION");
            case 2 -> ejecutarCuenta(new CuentaMutex(), "con MUTEX (ReentrantLock)");
            case 3 -> ejecutarCuenta(new CuentaMonitor(), "con MONITOR (synchronized)");
            case 4 -> ejecutarCuenta(new CuentaSemaforo(), "con SEMAFORO binario");
            case 5 -> CenaFilosofos.ejecutar();
            case 6 -> LectoresEscritores.ejecutar();
            default -> { return false; }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        // Modo directo: java Main <opcion>
        if (args.length == 1) {
            ejecutarOpcion(Integer.parseInt(args[0]));
            return;
        }
        Scanner teclado = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("===== SINCRONIZACION DE HILOS EN JAVA =====");
            System.out.println("1. Cuenta compartida SIN sincronizacion (condicion de carrera)");
            System.out.println("2. Cuenta compartida con Mutex (ReentrantLock)");
            System.out.println("3. Cuenta compartida con Monitor (synchronized)");
            System.out.println("4. Cuenta compartida con Semaforo binario");
            System.out.println("5. Cena de los filosofos (Semaforos)");
            System.out.println("6. Lectores-Escritores (Monitor)");
            System.out.println("0. Salir");
            System.out.print("Elija una opcion: ");
            if (!teclado.hasNextInt()) break;
            opcion = teclado.nextInt();
            if (opcion != 0 && !ejecutarOpcion(opcion)) {
                System.out.println("Opcion no valida.");
            }
        }
        System.out.println("Programa finalizado.");
    }
}
