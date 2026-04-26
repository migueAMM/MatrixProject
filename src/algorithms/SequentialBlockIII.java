package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 8: III.3 Sequential Block (Row by Column)
 *
 * Divide la multiplicación en bloques de tamaño BSIZE x BSIZE para mejorar
 * la localidad de caché. En lugar de recorrer toda la matriz de forma lineal,
 * se trabaja sobre bloques que caben en caché (L1/L2), reduciendo los fallos.
 *
 * La multiplicación sigue siendo fila x columna (A[i,k] * B[k,j] → C[i,j])
 * pero agrupada en bloques:
 *   for i1, j1, k1 en pasos de BSIZE:
 *     for i in [i1, i1+BSIZE):
 *       for j in [j1, j1+BSIZE):
 *         for k in [k1, k1+BSIZE):
 *           C[i,j] += A[i,k] * B[k,j]
 */
public class SequentialBlockIII implements MatrixAlgorithms {

    // Tamaño del bloque: 64 suele ser óptimo para cachés L1 de 32 KB con long (8 bytes)
    // 64 * 64 * 8 bytes = 32 KB por bloque
    private static final int BSIZE = 64;

    @Override
    public String getNombre() {
        return "8. III.3 Sequential Block (Row x Column)";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();

        if (matrizA.getColumnas() != matrizB.getFilas()) {
            throw new IllegalArgumentException("Dimensiones incompatibles.");
        }

        BigMatrix resultado = new BigMatrix(n, matrizB.getColumnas());

        for (int i1 = 0; i1 < n; i1 += BSIZE) {
            for (int j1 = 0; j1 < matrizB.getColumnas(); j1 += BSIZE) {
                for (int k1 = 0; k1 < matrizA.getColumnas(); k1 += BSIZE) {

                    // Límites reales del bloque (evita salirse de la matriz)
                    int iMax = Math.min(i1 + BSIZE, n);
                    int jMax = Math.min(j1 + BSIZE, matrizB.getColumnas());
                    int kMax = Math.min(k1 + BSIZE, matrizA.getColumnas());

                    for (int i = i1; i < iMax; i++) {
                        for (int j = j1; j < jMax; j++) {
                            long suma = resultado.getValue(i, j);
                            for (int k = k1; k < kMax; k++) {
                                suma += matrizA.getValue(i, k) * matrizB.getValue(k, j);
                            }
                            resultado.setValue(i, j, suma);
                        }
                    }
                }
            }
        }
        return resultado;
    }
}
