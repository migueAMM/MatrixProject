package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 10: III.5 Enhanced Parallel Block (Row by Column)
 *
 * Versión mejorada del Parallel Block III.4.
 * Divide explícitamente la matriz en dos mitades verticales (primera mitad y
 * segunda mitad de filas) y las procesa en dos hilos separados ejecutados
 * simultáneamente. Es fiel al pseudocódigo de referencia que
 * usa Parallel.Do con dos lambdas, cada una cubriendo su mitad de filas.
 */
public class EnhancedParallelBlockIII implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "10. III.5 Enhanced Parallel Block (Row x Column)";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        int cols = matrizB.getColumnas();
        int kSize = matrizA.getColumnas();

        if (kSize != matrizB.getFilas()) {
            throw new IllegalArgumentException("Dimensiones incompatibles.");
        }

        BigMatrix resultado = new BigMatrix(n, cols);
        int mitad = n / 2;

        // Dos hilos: cada uno procesa su mitad de filas
        Thread hilo1 = new Thread(() ->
            procesarBloques(matrizA, matrizB, resultado, 0,     mitad, cols, kSize));

        Thread hilo2 = new Thread(() ->
            procesarBloques(matrizA, matrizB, resultado, mitad, n,     cols, kSize));

        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return resultado;
    }

    /**
     * Procesa los bloques para el rango de filas [filaInicio, filaFin).
     * Lógica idéntica al Sequential Block III.3 pero solo sobre su mitad.
     */
    private void procesarBloques(BigMatrix A, BigMatrix B, BigMatrix R,
                                  int filaInicio, int filaFin, int cols, int kSize) {
        for (int i1 = filaInicio; i1 < filaFin; i1 += BSIZE) {
            for (int j1 = 0; j1 < cols; j1 += BSIZE) {
                for (int k1 = 0; k1 < kSize; k1 += BSIZE) {
                    int iMax = Math.min(i1 + BSIZE, filaFin);
                    int jMax = Math.min(j1 + BSIZE, cols);
                    int kMax = Math.min(k1 + BSIZE, kSize);

                    for (int i = i1; i < iMax; i++) {
                        for (int j = j1; j < jMax; j++) {
                            long suma = R.getValue(i, j);
                            for (int k = k1; k < kMax; k++) {
                                suma += A.getValue(i, k) * B.getValue(k, j);
                            }
                            R.setValue(i, j, suma);
                        }
                    }
                }
            }
        }
    }
}
