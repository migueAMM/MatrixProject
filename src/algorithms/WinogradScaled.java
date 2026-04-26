package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 5: Winograd Scaled
 *
 * Extiende WinogradOriginal escalando las filas de A antes de multiplicar.
 * El escalado normaliza los valores para mejorar la estabilidad numérica
 * y en algunos contextos reduce el error de redondeo en punto flotante.
 * Con long (enteros), la diferencia de resultado es equivalente al original,
 * pero la lógica de escalado se incluye tal como está definida en la literatura.
 *
 * Escala: para cada fila i de A, se calcula el máximo valor absoluto (lambda)
 * y se divide cada elemento por lambda antes del cálculo Winograd.
 * Al final se multiplica el resultado por lambda para restaurar la magnitud.
 */
public class WinogradScaled implements MatrixAlgorithms {

    @Override
    public String getNombre() {
        return "5. WinogradScaled";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        BigMatrix resultado = new BigMatrix(n, n);

        int mitad = n / 2;

        // 1. Calcular el factor de escala (lambda) para cada fila de A
        //    lambda[i] = max valor absoluto de la fila i
        long[] lambda = new long[n];
        for (int i = 0; i < n; i++) {
            long maxVal = 0;
            for (int k = 0; k < n; k++) {
                long abs = Math.abs(matrizA.getValue(i, k));
                if (abs > maxVal) maxVal = abs;
            }
            lambda[i] = (maxVal == 0) ? 1 : maxVal;
        }

        // 2. Pre-calcular rowFactor con valores escalados
        //    Se usa double internamente para la escala, luego se redondea
        double[] rowFactor = new double[n];
        for (int i = 0; i < n; i++) {
            double lam = lambda[i];
            for (int k = 0; k < mitad; k++) {
                double a1 = matrizA.getValue(i, 2 * k)     / lam;
                double a2 = matrizA.getValue(i, 2 * k + 1) / lam;
                rowFactor[i] += a1 * a2;
            }
        }

        // 3. Pre-calcular colFactor (B no se escala en la versión estándar)
        double[] colFactor = new double[n];
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < mitad; k++) {
                colFactor[j] += (double) matrizB.getValue(2 * k, j)
                              * matrizB.getValue(2 * k + 1, j);
            }
        }

        // 4. Producto principal con escalado
        for (int i = 0; i < n; i++) {
            double lam = lambda[i];
            for (int j = 0; j < n; j++) {
                double suma = -rowFactor[i] - colFactor[j] / (lam * lam);
                for (int k = 0; k < mitad; k++) {
                    double a1 = matrizA.getValue(i, 2 * k)     / lam;
                    double a2 = matrizA.getValue(i, 2 * k + 1) / lam;
                    double b1 = matrizB.getValue(2 * k,     j);
                    double b2 = matrizB.getValue(2 * k + 1, j);
                    suma += (a1 + b2 / lam) * (a2 + b1 / lam);
                }
                // Re-escalar para obtener el resultado real
                resultado.setValue(i, j, Math.round(suma * lam * lam));
            }
        }

        // 5. Ajuste si n es impar
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
