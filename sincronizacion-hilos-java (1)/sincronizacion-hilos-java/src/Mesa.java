import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Recurso compartido de la cena de los filosofos. Cada tenedor es un semaforo binario.
 * Para evitar la espera circular (interbloqueo), cada filosofo toma SIEMPRE primero el
 * tenedor de numero menor y despues el de numero mayor (jerarquia de recursos).
 */
public class Mesa {
    private final int n;
    private final Semaphore[] tenedores;
    private final AtomicBoolean[] comiendo;
    private final AtomicInteger comidasTotales = new AtomicInteger();
    private final AtomicInteger violaciones = new AtomicInteger();

    public Mesa(int n) {
        this.n = n;
        tenedores = new Semaphore[n];
        comiendo = new AtomicBoolean[n];
        for (int i = 0; i < n; i++) {
            tenedores[i] = new Semaphore(1);
            comiendo[i] = new AtomicBoolean(false);
        }
    }

    public int tenedorIzquierdo(int id) { return id; }
    public int tenedorDerecho(int id)   { return (id + 1) % n; }

    public void tomarTenedores(int id) throws InterruptedException {
        int primero = Math.min(tenedorIzquierdo(id), tenedorDerecho(id));
        int segundo = Math.max(tenedorIzquierdo(id), tenedorDerecho(id));
        tenedores[primero].acquire();
        tenedores[segundo].acquire();
    }

    public void soltarTenedores(int id) {
        tenedores[tenedorIzquierdo(id)].release();
        tenedores[tenedorDerecho(id)].release();
    }

    /** Verificacion independiente: dos filosofos vecinos nunca deben comer a la vez. */
    public void registrarInicioComida(int id) {
        comiendo[id].set(true);
        if (comiendo[(id + 1) % n].get() || comiendo[(id + n - 1) % n].get()) {
            violaciones.incrementAndGet();
        }
    }

    public void registrarFinComida(int id) {
        comiendo[id].set(false);
        comidasTotales.incrementAndGet();
    }

    public int getComidasTotales() { return comidasTotales.get(); }
    public int getViolaciones()    { return violaciones.get(); }
}
