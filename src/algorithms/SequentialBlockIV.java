package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 11: IV.3 Sequential Block (Row by Row)
 *
 * Método Row by Row con bloques. La diferencia con III.3 es el orden de
 * los índices internos: en lugar de A[i,k]*B[k,j] → C[i,j],
 * se usa A[i,j]*B[j,k] → C[i,k]
 *
 * Esto favorece el acceso secuencial a C y B (ambos por fila),
 * que en matrices almacenadas row-major es más amigable con la caché.
 *
 * Pseudocódigo del paper (IV.3):
 *   for i1, j1, k1 en pasos BSIZE:
 *     for i in [i1, i1+BSIZE):
 *       for j in [j1, j1+BSIZE):
 *         for k in [k1, k1+BSIZE):
 *           C[i,k] += A[i,j] * B[j,k]
 */
public class SequentialBlockIV implements MatrixAlgorithms {

    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "11. IV.3 Sequential Block (Row x Row)";
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

        // Bucles de bloques: i=filas de A, j=columnas de A / filas de B, k=columnas de B
        for (int i1 = 0; i1 < n; i1 += BSIZE) {
            for (int j1 = 0; j1 < kSize; j1 += BSIZE) {
                for (int k1 = 0; k1 < cols; k1 += BSIZE) {

                    int iMax = Math.min(i1 + BSIZE, n);
                    int jMax = Math.min(j1 + BSIZE, kSize);
                    int kMax = Math.min(k1 + BSIZE, cols);

                    // Orden row-by-row: C[i,k] += A[i,j] * B[j,k]
                    for (int i = i1; i < iMax; i++) {
                        for (int j = j1; j < jMax; j++) {
                            long valorA = matrizA.getValue(i, j);
                            for (int k = k1; k < kMax; k++) {
                                resultado.setValue(i, k,
                                    resultado.getValue(i, k) + valorA * matrizB.getValue(j, k));
                            }
                        }
                    }
                }
            }
        }
        return resultado;
    }
}
