/** Variante SIN proteccion: expone la condicion de carrera (lectura-modificacion-escritura). */
public class CuentaSinSincronizar implements CuentaBancaria {
    private int saldo = 0;

    @Override
    public void depositar(int monto) {
        int temporal = saldo;       // 1. leer
        temporal = temporal + monto; // 2. modificar
        saldo = temporal;           // 3. escribir (otro hilo pudo cambiar saldo entre 1 y 3)
    }

    @Override
    public int obtenerSaldo() {
        return saldo;
    }
}
