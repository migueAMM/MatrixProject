package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 4: Winograd Original
 *
 * Winograd reduce el número de multiplicaciones al pre-calcular productos
 * parciales de las filas de A y columnas de B. Para una multiplicación de
 * tamaño n, el número de multiplicaciones se reduce aproximadamente a la mitad.
 *
 * Idea central:
 *   (a1*b1 + a2*b2) = (a1+b2)*(a2+b1) - a1*a2 - b1*b2
 * Se pre-computan rowFactor[i] y colFactor[j] y se reutilizan.
 */
public class WinogradOriginal implements MatrixAlgorithms {

    @Override
    public String getNombre() {
        return "4. WinogradOriginal";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        BigMatrix resultado = new BigMatrix(n, n);

        int mitad = n / 2;

        // 1. Pre-calcular rowFactor para cada fila de A
        long[] rowFactor = new long[n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < mitad; k++) {
                rowFactor[i] += matrizA.getValue(i, 2 * k) * matrizA.getValue(i, 2 * k + 1);
            }
        }

        // 2. Pre-calcular colFactor para cada columna de B
        long[] colFactor = new long[n];
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < mitad; k++) {
                colFactor[j] += matrizB.getValue(2 * k, j) * matrizB.getValue(2 * k + 1, j);
            }
        }

        // 3. Calcular el producto usando los factores pre-computados
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long suma = -rowFactor[i] - colFactor[j];
                for (int k = 0; k < mitad; k++) {
                    suma += (matrizA.getValue(i, 2 * k)     + matrizB.getValue(2 * k + 1, j))
                          * (matrizA.getValue(i, 2 * k + 1) + matrizB.getValue(2 * k,     j));
                }
                resultado.setValue(i, j, suma);
            }
        }

        // 4. Si n es impar, hay una columna/fila extra que procesar
        if (n % 2 != 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    long val = resultado.getValue(i, j)
                             + matrizA.getValue(i, n - 1) * matrizB.getValue(n - 1, j);
                    resultado.setValue(i, j, val);
                }
            }
        }

        return resultado;
    }
}
