package algorithms;

import models.BigMatrix;

public class NaivLoopUnrollingTwo implements MatrixAlgorithms{
    @Override
    public String getNombre() {
        return "2. NaivLoopUnrollingTwo";
    }

    /***
     * Este metodo funciona igual que el NaivOnArray pero su diferencia es que procesa
     * 2 iteraciones al mismo tiempo que seria k y k+1 el limite este caso seria n-1
     * para garantizar que k+1 no se salga de la matriz
     * @param matrizA Primera matriz a multiplicar
     * @param matrizB Segunda matriz a multiplicar
     * @return
     */
    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        BigMatrix resultado = new BigMatrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long suma = 0;
                int k;

                /* Desenrollado de a 2:
                   Procesamos k y k+1 en la misma iteración.
                   El límite es n - 1 para asegurar que k+1 no se salga de la matriz.
                */
                for (k = 0; k < n - 1; k += 2) {
                    suma += matrizA.getValue(i, k) * matrizB.getValue(k, j);
                    suma += matrizA.getValue(i, k + 1) * matrizB.getValue(k + 1, j);
                }

                // Si n es impar o quedó un elemento pendiente, lo sumamos aquí
                for (; k < n; k++) {
                    suma += matrizA.getValue(i, k) * matrizB.getValue(k, j);
                }

                resultado.setValue(i, j, suma);
            }
        }
        return resultado;
    }

}
