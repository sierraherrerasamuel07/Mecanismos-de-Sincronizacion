/** Variante con Monitor implicito de Java: synchronized convierte al objeto en monitor. */
public class CuentaMonitor implements CuentaBancaria {
    private int saldo = 0;

    @Override
    public synchronized void depositar(int monto) {
        saldo = saldo + monto;
    }

    @Override
    public synchronized int obtenerSaldo() {
        return saldo;
    }
}
