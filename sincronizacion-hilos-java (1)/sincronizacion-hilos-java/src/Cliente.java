/** Hilo que realiza una cantidad fija de depositos sobre una cuenta compartida. */
public class Cliente extends Thread {
    private final CuentaBancaria cuenta;
    private final int operaciones;
    private final int monto;

    public Cliente(String nombre, CuentaBancaria cuenta, int operaciones, int monto) {
        super(nombre);
        this.cuenta = cuenta;
        this.operaciones = operaciones;
        this.monto = monto;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < operaciones; i++) {
                cuenta.depositar(monto);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
