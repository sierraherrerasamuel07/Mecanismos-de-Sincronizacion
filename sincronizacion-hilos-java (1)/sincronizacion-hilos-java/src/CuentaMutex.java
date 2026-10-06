import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Variante con Mutex explicito (ReentrantLock): el hilo pide y libera el candado manualmente. */
public class CuentaMutex implements CuentaBancaria {
    private int saldo = 0;
    private final Lock candado = new ReentrantLock();

    @Override
    public void depositar(int monto) {
        candado.lock();
        try {
            saldo = saldo + monto;   // seccion critica
        } finally {
            candado.unlock();        // siempre se libera, incluso si hay una excepcion
        }
    }

    @Override
    public int obtenerSaldo() {
        candado.lock();
        try {
            return saldo;
        } finally {
            candado.unlock();
        }
    }
}
