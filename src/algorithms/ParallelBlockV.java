package algorithms;

import models.BigMatrix;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Algoritmo 15: V.4 Parallel Block (Column by Column)
 *
 * Versión paralela de V.3 usando ForkJoinPool.
 * Se paraleliza sobre los bloques de columnas del resultado (i1),
 * ya que cada columna del resultado es independiente de las demás.
 * Fórmula: C[k,i] += A[k,j] * B[j,i]
 */
public class ParallelBlockV implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "15. V.4 Parallel Block (Column x Column)";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        int cols = matrizB.getColumnas();
        int shared = matrizA.getColumnas();

        if (shared != matrizB.getFilas()) {
            throw new IllegalArgumentException("Dimensiones incompatibles.");
        }

        BigMatrix resultado = new BigMatrix(n, cols);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new BlockTaskV(matrizA, matrizB, resultado, 0, cols, n, shared));
        pool.shutdown();

        return resultado;
    }

    private static class BlockTaskV extends RecursiveAction {
        private final BigMatrix A, B, R;
        private final int i1Start, i1End, n, shared;

        BlockTaskV(BigMatrix A, BigMatrix B, BigMatrix R,
                   int i1Start, int i1End, int n, int shared) {
            this.A = A; this.B = B; this.R = R;
            this.i1Start = i1Start; this.i1End = i1End;
            this.n = n; this.shared = shared;
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
                new BlockTaskV(A, B, R, i1Start, mid,   n, shared),
                new BlockTaskV(A, B, R, mid,     i1End, n, shared)
            );
        }

        private void computeDirect() {
            // i1 = bloque de columnas del resultado
            for (int i1 = i1Start; i1 < i1End; i1 += BSIZE) {
                for (int j1 = 0; j1 < shared; j1 += BSIZE) {
                    for (int k1 = 0; k1 < n; k1 += BSIZE) {
                        int iMax = Math.min(i1 + BSIZE, i1End);
                        int jMax = Math.min(j1 + BSIZE, shared);
                        int kMax = Math.min(k1 + BSIZE, n);

                        // C[k,i] += A[k,j] * B[j,i]
                        for (int i = i1; i < iMax; i++) {
                            for (int j = j1; j < jMax; j++) {
                                long valorB = B.getValue(j, i);
                                for (int k = k1; k < kMax; k++) {
                                    R.setValue(k, i,
                                        R.getValue(k, i) + A.getValue(k, j) * valorB);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
