package algorithms;

import models.BigMatrix;

public class NaivLoopUnrollingFour implements MatrixAlgorithms {

    @Override
    public String getNombre() {
        return "3. NaivLoopUnrollingFour";
    }

    /**
     * Similar al NaivLoopUnrollingTwo pero procesa 4 iteraciones de k al mismo tiempo
     * (k, k+1, k+2, k+3), reduciendo el overhead del ciclo for a la cuarta parte.
     * El límite del ciclo principal es n-3 para que k+3 nunca se salga del arreglo.
     */
    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        BigMatrix resultado = new BigMatrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long suma = 0;
                int k;

                // Desenrollado de a 4
                for (k = 0; k < n - 3; k += 4) {
                    suma += matrizA.getValue(i, k)     * matrizB.getValue(k,     j);
                    suma += matrizA.getValue(i, k + 1) * matrizB.getValue(k + 1, j);
                    suma += matrizA.getValue(i, k + 2) * matrizB.getValue(k + 2, j);
                    suma += matrizA.getValue(i, k + 3) * matrizB.getValue(k + 3, j);
                }

                // Manejar los elementos restantes (cuando n no es múltiplo de 4)
                for (; k < n; k++) {
                    suma += matrizA.getValue(i, k) * matrizB.getValue(k, j);
                }

                resultado.setValue(i, j, suma);
            }
        }
        return resultado;
    }
}
