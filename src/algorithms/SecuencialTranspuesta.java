package algorithms;

import models.BigMatrix;

public class SecuencialTranspuesta implements MatrixAlgorithms{
    @Override
    public String getNombre() {
        return "18. Secuencial con la matriz B transpuesta";
    }

    /***
     * Al trasnsponer B ahora sus antiguas columnas son sus nuevas filas, en este algortimo
     * estamos multiplicando filas de A por filas de B lo cual se hace mucho mas comodo para el procesador
     * @param matrizA Primera matriz a multiplicar
     * @param matrizB Segunda matriz a multiplicar
     * @return
     */
    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {

        int filasA = matrizA.getFilas();
        int columnasA = matrizA.getColumnas();
        int filasB = matrizB.getFilas();
        int columnasB = matrizB.getColumnas();

        if(columnasA != filasB){
            throw new IllegalArgumentException("Erros, las columnas de A deben ser iguales que las filas de B");
        }
        BigMatrix resultado = new BigMatrix(filasA , columnasB);
        //Ahora vamos a transponer la matriz B
        BigMatrix transpuestaB = new BigMatrix(filasA , columnasB);
        for (int i = 0 ; i < filasB ; i++){
            for (int j = 0 ; j < columnasB ; j++){
                //Aca cambiamos el orden para intercambiar las filas por columnas
                transpuestaB.setValue(j, i , matrizB.getValue(i, j));
            }
        }
        //Ahora vamos con la multiplicacion
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {

                // Usamos una variable temporal para ir sumando.
                long sumaTemp = 0;

                for (int k = 0; k < columnasA; k++) {
                    sumaTemp += matrizA.getValue(i, k) * transpuestaB.getValue(j, k);
                }

                // Una vez terminado el cálculo de la celda, guardamos el total
                resultado.setValue(i, j, sumaTemp);
            }
        }
        return resultado;
    }
}
