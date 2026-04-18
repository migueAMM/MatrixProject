package algorithms;

import models.BigMatrix;

public class SecuencialIngenuo implements MatrixAlgorithms{


    @Override
    public String getNombre() {
        return "1. Algoritmo Secuencial Ingenuo (Fuerza Bruta)";
    }

    /***
     * Este algoritmo es el clasico, multiplicamos filas por columnas de esta forma
     * 0,0*0.0 + 0.1*1,0 + 0,2*2,0 + 0,3*3,0 ... 
     * @param matrizA Primera matriz a multiplicar
     * @param matrizB Segunda matriz a multiplicar
     * @return
     */
    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {

        int filasA = matrizA.getFilas();
        int columnasA = matrizA.getColumnas(); //ColimnasA debe ser igual a filasB
        int columnasB = matrizB.getColumnas();
        int filasB = matrizB.getFilas();

        //Validamos que si sean iguales
        if(columnasA != filasB){
            throw new IllegalArgumentException("Error, las columnas de A deben ser iguales a las filas de B");
        }

        //Creamos la matriz donde se almacenara el resultado
        BigMatrix result = new BigMatrix(filasA , columnasB);

        //Los 3 ciclos clasicos para multiplicar matrices (i,j,k)
        for (int i = 0; i < filasA; i++) {           // Recorre filas de A
            for (int j = 0; j < columnasB; j++) {    // Recorre columnas de B

                long sumaTemp = 0; // Acumulador para la celda actual

                for (int k = 0; k < columnasA; k++) { // Multiplica y suma
                    sumaTemp += matrizA.getValue(i, k) * matrizB.getValue(k, j);
                }

                result.setValue(i, j, sumaTemp);
            }
        }

        return result;

    }
}
