package generators;
import models.BigMatrix;
import java.util.Random;

public class MatrixGenerator {

    public static BigMatrix generateRandom (int filas, int columnas){

    BigMatrix matriz = new BigMatrix(filas, columnas);
    Random random = new Random();

    //Definimos los limites escogidos, llenando la matriz con numeros entre 10000 y 250000
    int min = 10000;
    int max = 250000;

    for (int i = 0 ; i < filas ; i++){
        for (int j = 0 ; j < columnas ; j++){
            long randomNum = random.nextInt((max-min) + 1) + min; //Generando numero aleatorio entre los numeros escogidos
            matriz.setValue(i, j , randomNum);
        }
    }
    return matriz;
    }

}
