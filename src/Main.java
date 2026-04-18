import models.BigMatrix;
import generators.MatrixGenerator;

public class Main {

    static void main() {
        //Quemamos un tamaño provisional de las matrices de 1000x1000
        int filas = 1000;
        int columnas = 1000;

        System.out.println("Generando matriz de: " + filas + " filas y " + columnas);

        //Generamos las matrices
        BigMatrix matrizA = MatrixGenerator.generateRandom(filas, columnas);
        BigMatrix matrizB = MatrixGenerator.generateRandom(filas, columnas);

        System.out.println("Matrices generadas con éxito");

        //Visualizacion de las matrices mostrando solo un pedazo
        //matrizA.mostrarResumen(10);  //mostramos un segmento de 10x10
        //matrizB.mostrarResumen(10);
    }

}