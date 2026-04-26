package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 13: IV.5 Enhanced Parallel Block (Row by Row)
 *
 * Versión mejorada: dos hilos explícitos, cada uno con su mitad de filas.
 * Fiel al Parallel.Do de dos lambdas.
 * Fórmula interna: C[i,k] += A[i,j] * B[j,k]  (Row x Row)
 */
public class EnhancedParallelBlockIV implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "13. IV.5 Enhanced Parallel Block (Row x Row)";
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

    private void procesarBloques(BigMatrix A, BigMatrix B, BigMatrix R,
                                  int filaInicio, int filaFin, int cols, int kSize) {
        for (int i1 = filaInicio; i1 < filaFin; i1 += BSIZE) {
            for (int j1 = 0; j1 < kSize; j1 += BSIZE) {
                for (int k1 = 0; k1 < cols; k1 += BSIZE) {
                    int iMax = Math.min(i1 + BSIZE, filaFin);
                    int jMax = Math.min(j1 + BSIZE, kSize);
                    int kMax = Math.min(k1 + BSIZE, cols);

                    for (int i = i1; i < iMax; i++) {
                        for (int j = j1; j < jMax; j++) {
                            long valorA = A.getValue(i, j);
                            for (int k = k1; k < kMax; k++) {
                                R.setValue(i, k,
                                    R.getValue(i, k) + valorA * B.getValue(j, k));
                            }
                        }
                    }
                }
            }
        }
    }
}
