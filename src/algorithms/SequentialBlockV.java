package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 14: V.3 Sequential Block (Column by Column)
 *
 * Método Column by Column con bloques.
 * Fórmula interna: C[k,i] += A[k,j] * B[j,i]
 *
 * Los índices cambian respecto a los anteriores:
 *   - i recorre columnas del resultado (columnas de B)
 *   - j recorre la dimensión compartida
 *   - k recorre filas del resultado (filas de A)
 *
 * Pseudocódigo del paper (V.3):
 *   for i1, j1, k1 en pasos BSIZE:
 *     for i in [i1, i1+BSIZE):        ← columna de resultado
 *       for j in [j1, j1+BSIZE):      ← dimensión compartida
 *         for k in [k1, k1+BSIZE):    ← fila de resultado
 *           C[k,i] += A[k,j] * B[j,i]
 */
public class SequentialBlockV implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "14. V.3 Sequential Block (Column x Column)";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        int cols = matrizB.getColumnas();
        int shared = matrizA.getColumnas();   // = filas de B

        if (shared != matrizB.getFilas()) {
            throw new IllegalArgumentException("Dimensiones incompatibles.");
        }

        BigMatrix resultado = new BigMatrix(n, cols);

        // i1 = bloque de columnas del resultado
        // j1 = bloque de la dimensión compartida
        // k1 = bloque de filas del resultado
        for (int i1 = 0; i1 < cols; i1 += BSIZE) {
            for (int j1 = 0; j1 < shared; j1 += BSIZE) {
                for (int k1 = 0; k1 < n; k1 += BSIZE) {

                    int iMax = Math.min(i1 + BSIZE, cols);
                    int jMax = Math.min(j1 + BSIZE, shared);
                    int kMax = Math.min(k1 + BSIZE, n);

                    // C[k,i] += A[k,j] * B[j,i]
                    for (int i = i1; i < iMax; i++) {
                        for (int j = j1; j < jMax; j++) {
                            long valorB = matrizB.getValue(j, i);
                            for (int k = k1; k < kMax; k++) {
                                resultado.setValue(k, i,
                                    resultado.getValue(k, i) + matrizA.getValue(k, j) * valorB);
                            }
                        }
                    }
                }
            }
        }
        return resultado;
    }
}
