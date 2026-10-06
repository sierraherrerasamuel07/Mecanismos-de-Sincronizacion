# Sincronización de hilos en Java

Proyecto del curso de Sistemas operativos (Universidad Santiago de Cali).
Muestra tres mecanismos de sincronización (Mutex explícito, Semáforos y Monitores) más `join()`.

## Escenarios (menú de consola)
1. Cuenta compartida SIN sincronización (condición de carrera)
2. Cuenta compartida con Mutex (`ReentrantLock`)
3. Cuenta compartida con Monitor (`synchronized`)
4. Cuenta compartida con Semáforo binario
5. Cena de los filósofos (Semáforos, jerarquía de recursos)
6. Lectores-Escritores (Monitor con `wait()` / `notifyAll()`)

## Cómo compilar y ejecutar
Requiere JDK 17 o superior.

    cd src
    javac *.java
    java Main          # menú interactivo
    java Main 5        # ejecuta directamente la opción 5

Nota: la opción 1 es no determinista; a veces el saldo coincide por azar y a veces se pierden depósitos.
