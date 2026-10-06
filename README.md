# Sincronización de hilos en Java

Laboratorio de **Sistemas operativos** — Ingeniería de Sistemas Virtual, Universidad Santiago de Cali.

**Autor:** Samuel Sierra Herrera
**Docente:** David Parra Cardona

Programa de consola en Java que usa varios hilos y tres mecanismos de sincronización
(**Mutex explícito**, **Semáforos** y **Monitores**), además de `join()`, aplicados a tres problemas de concurrencia.

## Escenarios

El programa tiene un menú interactivo con seis opciones:

| Opción | Escenario | Mecanismo |
|:-:|---|---|
| 1 | Cuenta bancaria compartida **sin** sincronización (muestra la condición de carrera) | Ninguno |
| 2 | Cuenta bancaria compartida | Mutex (`ReentrantLock`) |
| 3 | Cuenta bancaria compartida | Monitor (`synchronized`) |
| 4 | Cuenta bancaria compartida | Semáforo binario (`Semaphore`) |
| 5 | Cena de los filósofos | Semáforos (un semáforo por tenedor) |
| 6 | Lectores-Escritores | Monitor con `wait()` / `notifyAll()` |

En todos los escenarios, el hilo principal usa `join()` para esperar a que terminen los hilos de trabajo antes de verificar los resultados.

## Requisitos

- JDK 17 o superior (probado con OpenJDK 21).
- No usa librerías externas.

## Cómo compilar y ejecutar

```bash
cd src
javac *.java
java Main          # menú interactivo
java Main 5        # ejecuta directamente una opción (1 a 6)
```

## Estructura del código

```
src/
├── Main.java                  Menú de consola y ejecución de la cuenta bancaria
├── CuentaBancaria.java        Interfaz común de las cuatro variantes de cuenta
├── CuentaSinSincronizar.java  Sin protección (condición de carrera)
├── CuentaMutex.java           ReentrantLock
├── CuentaMonitor.java         synchronized
├── CuentaSemaforo.java        Semaphore binario
├── Cliente.java               Hilo que realiza depósitos
├── CenaFilosofos.java         Arma y ejecuta la cena de los filósofos
├── Mesa.java                  Tenedores (semáforos) y verificaciones
├── Filosofo.java              Hilo que alterna entre pensar y comer
├── LectoresEscritores.java    Arma y ejecuta el problema lectores-escritores
├── BaseDatosCompartida.java   Monitor con el protocolo de lectores y escritores
├── Lector.java                Hilo lector
└── Escritor.java              Hilo escritor
```

Los hilos `Cliente`, `Filosofo`, `Lector` y `Escritor` heredan de `Thread`.
Los hilos `Cliente` solo conocen la interfaz `CuentaBancaria`, por lo que funcionan sin cambios con cualquiera de las cuatro variantes.

## Qué verifica cada demostración

- **Cuenta bancaria (opciones 1 a 4):** 4 clientes hacen 1.000.000 de depósitos de 1 unidad cada uno.
  El saldo esperado es **4.000.000**; el programa compara el saldo obtenido contra ese valor e imprime cuántos depósitos se perdieron, si hubo pérdida.
- **Filósofos (opción 5):** 5 filósofos, 3 comidas cada uno. Cada filósofo toma siempre primero el tenedor de menor número, lo que elimina la espera circular y evita el interbloqueo.
  Se verifica que se realicen las 15 comidas y que nunca coman a la vez dos filósofos vecinos.
- **Lectores-Escritores (opción 6):** 3 lectores (3 lecturas cada uno) y 2 escritores (3 escrituras cada uno).
  Se permiten varios lectores simultáneos, pero un escritor entra siempre en exclusión.
  Se verifica el valor final del registro (esperado: 6) y que ningún lector observe datos a medio escribir.

## Notas

- **La opción 1 es no determinista.** La pérdida de depósitos depende del planificador y del número de núcleos del equipo.
  En equipos con varios núcleos suele aparecer casi siempre, en entornos de un solo núcleo puede coincidir con el valor esperado por azar.
  Ejecutar varias veces.
- **Los tiempos que imprime el programa son solo referenciales.** Los afectan el compilador JIT de la JVM y el planificador del sistema operativo, no son un benchmark formal.
- **El orden de los mensajes de consola puede variar** entre ejecuciones, porque depende del planificador. Lo que no debe variar son los resultados finales.
- **Prioridad a escritores:** en el problema de lectores y escritores, los escritores en espera tienen prioridad sobre los lectores nuevos.
  Con un flujo continuo de escritores, los lectores podrían sufrir inanición, en la demostración la carga es finita, así que todos los hilos terminan.
