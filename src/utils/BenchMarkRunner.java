package utils;

import algorithms.MatrixAlgorithms;
import models.BigMatrix;

public class BenchMarkRunner {


    /***
     * Este metodo recibe cualquiera de los algortimos que implemente la interfaz,
     * lo ejecuta y mide su tiempo exacto de ejecucion
     * @param algortimo
     * @param matrizA
     * @param matrizB
     * @return
     */
    public static BigMatrix evaluarAlgoritmo (MatrixAlgorithms algortimo , BigMatrix matrizA , BigMatrix matrizB){

        System.out.println("Iniciando prueba del algoritmo: " + algortimo.getNombre());

        long tiempoInicio = System.currentTimeMillis(); //Iniciamos el cronometro en ms
        BigMatrix result = algortimo.multiplicar(matrizA, matrizB); //Ejecutamos el codigo
        //Ahora detenemos el cronometro
        long tiempoFin = System.currentTimeMillis();
        long tiempoTotal = tiempoFin - tiempoInicio;

        System.out.println("Tiempo de ejecucion del algoritmo: " + algortimo.getNombre() + "es de " + tiempoTotal + "ms");

        return matrizA;
    }

}
