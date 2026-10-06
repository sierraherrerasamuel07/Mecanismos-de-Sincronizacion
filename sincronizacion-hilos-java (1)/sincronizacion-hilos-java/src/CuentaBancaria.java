/**
 * Contrato comun de las cuatro variantes de cuenta compartida.
 * Los hilos Cliente solo conocen esta interfaz, no el mecanismo de sincronizacion.
 */
public interface CuentaBancaria {
    void depositar(int monto) throws InterruptedException;
    int obtenerSaldo() throws InterruptedException;
}
