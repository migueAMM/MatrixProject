package algorithms;

import models.BigMatrix;

public class SecuencialOptimizado implements MatrixAlgorithms{
    @Override
    public String getNombre() {
        return "2. Secuencial Optimizado por Caché (i , k , j ) ";
    }

    /***
     * En Java (y en la mayoría de los lenguajes), las matrices se guardan en la memoria RAM fila por fila (horizontalmente).
     * En este Algoritmo 2 (i, k, j), simplemente cambiamos el orden de los ciclos for. Al poner la j al final, obligamos a
     * a leer ambas matrices y la matriz de resultado horizontalmente. El procesador lee los datos de corrido, se accede menos a la
     * memoria RAM y usamos mas la memoria cache por lo que los datos se obtienen mucho mas rapido
     * @param matrizA Primera matriz a multiplicar
     * @param matrizB Segunda matriz a multiplicar
     * @return
     */
    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {

        int filasA = matrizA.getFilas();
        int columnasA = matrizA.getColumnas();
        int columnasB = matrizB.getColumnas();

        if (columnasA != matrizB.getFilas()) {
            throw new IllegalArgumentException("Error: Las columnas de A deben coincidir con las filas de B.");
        }

        BigMatrix resultado = new BigMatrix(filasA, columnasB);

        // El orden de los ciclos cambia a i, k, j
        for (int i = 0; i < filasA; i++) {
            for (int k = 0; k < columnasA; k++) {

                // Extraemos el valor de A(i, k) una sola vez por cada iteración de k
                long valorA = matrizA.getValue(i, k);

                for (int j = 0; j < columnasB; j++) {
                    // Multiplicamos y sumamos al valor que ya existía en la celda del resultado
                    long valorActual = valorA * matrizB.getValue(k , j);
                    resultado.sumatoriaValor(i, j , valorActual);
                }
            }
        }

        return resultado;
    }
}
