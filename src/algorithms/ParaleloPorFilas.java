package algorithms;

import models.BigMatrix;

public class ParaleloPorFilas implements MatrixAlgorithms{
    @Override
    public String getNombre() {
        return "4. Paralelo por filas (Usando multihilos basico (Hilos nativos))";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {

        int filasA = matrizA.getFilas();
        int columnasA = matrizA.getColumnas();
        int columnasB = matrizB.getColumnas();

        if (columnasA != matrizB.getFilas()) {
            throw new IllegalArgumentException("Error, dimensiones incorrectas.");
        }

        BigMatrix resultado = new BigMatrix(filasA, columnasB);

        // 1. Preguntarle a la PC cuántos núcleos (hilos lógicos) hay disponibles para usar
        int numHilos = Runtime.getRuntime().availableProcessors();
        Thread[] hilos = new Thread[numHilos];

        // 2. Calcular cuántas filas debe procesar cada hilo
        int filasPorHilo = filasA / numHilos;

        // 3. Crear y lanzar los hilos
        for (int i = 0; i < numHilos; i++) {
            final int inicioFila = i * filasPorHilo;

            // Si es el último hilo, le damos el residuo de filas por si la división no fue exacta
            final int finFila = (i == numHilos - 1) ? filasA : (i + 1) * filasPorHilo;

            hilos[i] = new Thread(() -> {
                // --- LÓGICA DEL SECUENCIAL OPTIMIZADO ---
                // Fíjate que en lugar de empezar en 0, este hilo empieza en 'inicioFila'
                for (int fila = inicioFila; fila < finFila; fila++) {
                    for (int k = 0; k < columnasA; k++) {
                        long valorA = matrizA.getValue(fila, k);
                        for (int col = 0; col < columnasB; col++) {
                            long multiplicacion = valorA * matrizB.getValue(k, col);
                            // Usamos el método que creaste en el paso anterior
                            resultado.sumatoriaValor(fila, col, multiplicacion);
                        }
                    }
                }
            });

            // ¡Arranca el hilo y ponlo a trabajar al mismo tiempo que los demás!
            hilos[i].start();
        }

        // 4. El hilo principal (Main) debe ESPERAR a que todos los trabajadores terminen
        for (int i = 0; i < numHilos; i++) {
            try {
                // join() pone pausa al programa principal hasta que este hilo termine su parte
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 5. Solo cuando todos terminaron, entregamos la matriz ensamblada

        return resultado;
    }
}
