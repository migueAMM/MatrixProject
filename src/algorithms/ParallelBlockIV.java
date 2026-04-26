package algorithms;

import models.BigMatrix;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Algoritmo 12: IV.4 Parallel Block (Row by Row)
 *
 * Versión paralela de IV.3. Se paraleliza sobre los bloques de filas (i1)
 * usando ForkJoinPool. Cada tarea procesa su rango de i1 con todos los
 * bloques j1 y k1, usando la fórmula row-by-row: C[i,k] += A[i,j]*B[j,k].
 */
public class ParallelBlockIV implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "12. IV.4 Parallel Block (Row x Row)";
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

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new BlockTaskIV(matrizA, matrizB, resultado, 0, n, cols, kSize));
        pool.shutdown();

        return resultado;
    }

    private static class BlockTaskIV extends RecursiveAction {
        private final BigMatrix A, B, R;
        private final int i1Start, i1End, cols, kSize;

        BlockTaskIV(BigMatrix A, BigMatrix B, BigMatrix R,
                    int i1Start, int i1End, int cols, int kSize) {
            this.A = A; this.B = B; this.R = R;
            this.i1Start = i1Start; this.i1End = i1End;
            this.cols = cols; this.kSize = kSize;
        }

        @Override
        protected void compute() {
            if ((i1End - i1Start) <= BSIZE) {
                computeDirect();
                return;
            }
            int mid = i1Start + ((i1End - i1Start) / 2 / BSIZE) * BSIZE;
            if (mid == i1Start) mid = i1Start + BSIZE;
            invokeAll(
                new BlockTaskIV(A, B, R, i1Start, mid,   cols, kSize),
                new BlockTaskIV(A, B, R, mid,     i1End, cols, kSize)
            );
        }

        private void computeDirect() {
            for (int i1 = i1Start; i1 < i1End; i1 += BSIZE) {
                for (int j1 = 0; j1 < kSize; j1 += BSIZE) {
                    for (int k1 = 0; k1 < cols; k1 += BSIZE) {
                        int iMax = Math.min(i1 + BSIZE, i1End);
                        int jMax = Math.min(j1 + BSIZE, kSize);
                        int kMax = Math.min(k1 + BSIZE, cols);

                        // Row x Row: C[i,k] += A[i,j] * B[j,k]
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
}
