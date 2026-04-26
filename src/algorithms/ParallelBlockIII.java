package algorithms;

import models.BigMatrix;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Algoritmo 9: III.4 Parallel Block (Row by Column)
 *
 * Versión paralela del Sequential Block III.3.
 * Se utiliza ForkJoinPool para distribuir los bloques de filas (i1)
 * entre múltiples hilos, manteniendo la misma lógica de bloques.
 *
 * Cada tarea del pool procesa un bloque de filas completo (todos los j1 y k1).
 */
public class ParallelBlockIII implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "9. III.4 Parallel Block (Row x Column)";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        int cols = matrizB.getColumnas();

        if (matrizA.getColumnas() != matrizB.getFilas()) {
            throw new IllegalArgumentException("Dimensiones incompatibles.");
        }

        BigMatrix resultado = new BigMatrix(n, cols);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new BlockTask(matrizA, matrizB, resultado, 0, n, cols, matrizA.getColumnas()));
        pool.shutdown();

        return resultado;
    }

    // ── Tarea recursiva para ForkJoin ─────────────────────────────────────────

    private static class BlockTask extends RecursiveAction {
        private final BigMatrix A, B, R;
        private final int i1Start, i1End, cols, kSize;

        BlockTask(BigMatrix A, BigMatrix B, BigMatrix R,
                  int i1Start, int i1End, int cols, int kSize) {
            this.A = A; this.B = B; this.R = R;
            this.i1Start = i1Start; this.i1End = i1End;
            this.cols = cols; this.kSize = kSize;
        }

        @Override
        protected void compute() {
            // Si el rango de filas a procesar es pequeño, ejecutar directamente
            if ((i1End - i1Start) <= BSIZE) {
                computeDirect();
                return;
            }
            // Dividir en dos mitades
            int mid = i1Start + ((i1End - i1Start) / 2 / BSIZE) * BSIZE;
            if (mid == i1Start) mid = i1Start + BSIZE;
            invokeAll(
                new BlockTask(A, B, R, i1Start, mid,   cols, kSize),
                new BlockTask(A, B, R, mid,     i1End, cols, kSize)
            );
        }

        private void computeDirect() {
            for (int i1 = i1Start; i1 < i1End; i1 += BSIZE) {
                for (int j1 = 0; j1 < cols; j1 += BSIZE) {
                    for (int k1 = 0; k1 < kSize; k1 += BSIZE) {
                        int iMax = Math.min(i1 + BSIZE, i1End);
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
}
