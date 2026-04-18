package algorithms;
import models.BigMatrix;

public interface MatrixAlgorithms {

    /***
     * Devuelve el nombre del algoritmo para mostrarlo
     */
    String getNombre();

    /***
     * El contrato principal, se reciben dos matrices y se devuelve el resultado
     * @param matrizA Primera matriz a multiplicar
     * @param matrizB Segunda matriz a multiplicar
     * @return
     */
    BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB);

}