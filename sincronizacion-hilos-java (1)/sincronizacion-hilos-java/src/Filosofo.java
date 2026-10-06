import java.util.concurrent.ThreadLocalRandom;

/** Hilo que alterna entre pensar y comer; para comer necesita sus dos tenedores. */
public class Filosofo extends Thread {
    private final int id;
    private final Mesa mesa;
    private final int comidas;

    public Filosofo(int id, Mesa mesa, int comidas) {
        super("Filosofo-" + id);
        this.id = id;
        this.mesa = mesa;
        this.comidas = comidas;
    }

    private static void dormirAleatorio() throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(50, 151));
    }

    @Override
    public void run() {
        try {
            for (int ronda = 1; ronda <= comidas; ronda++) {
                System.out.println("Filosofo " + id + " esta pensando.");
                dormirAleatorio();

                mesa.tomarTenedores(id);
                mesa.registrarInicioComida(id);
                System.out.println("[Semaforo] Filosofo " + id + " toma los tenedores " + mesa.tenedorIzquierdo(id)
                        + " y " + mesa.tenedorDerecho(id) + " y come (comida " + ronda + "/" + comidas + ").");
                dormirAleatorio();
                mesa.registrarFinComida(id);
                System.out.println("[Semaforo] Filosofo " + id + " termina de comer y suelta los tenedores.");
                mesa.soltarTenedores(id);   // el mensaje se imprime ANTES de liberar, para que el registro sea fiel
            }
            System.out.println("Filosofo " + id + " termino de cenar.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
