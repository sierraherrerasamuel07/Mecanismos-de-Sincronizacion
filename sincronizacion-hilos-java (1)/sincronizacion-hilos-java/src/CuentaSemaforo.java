import java.util.concurrent.Semaphore;

/** Variante con Semaforo binario (valor inicial 1) usado como cerrojo de exclusion mutua. */
public class CuentaSemaforo implements CuentaBancaria {
    private int saldo = 0;
    private final Semaphore semaforo = new Semaphore(1);

    @Override
    public void depositar(int monto) throws InterruptedException {
        semaforo.acquire();          // operacion wait / P
        try {
            saldo = saldo + monto;
        } finally {
            semaforo.release();      // operacion signal / V
        }
    }

    @Override
    public int obtenerSaldo() throws InterruptedException {
        semaforo.acquire();
        try {
            return saldo;
        } finally {
            semaforo.release();
        }
    }
}
