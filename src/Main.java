import models.BigMatrix;
import generators.MatrixGenerator;
import algorithms.SecuencialIngenuo;
import utils.BenchMarkRunner;

public class Main {

    static void main() {
        //Quemamos un tamaño provisional de las matrices de 1000x1000
        int filas = 2000;
        int columnas = 2000;

        System.out.println("Generando matriz de: " + filas + " filas y " + columnas);
        //Generamos las matrices
        BigMatrix matrizA = MatrixGenerator.generateRandom(filas, columnas);
        BigMatrix matrizB = MatrixGenerator.generateRandom(filas, columnas);
        System.out.println("Matrices generadas con éxito");

        //Visualizacion de las matrices mostrando solo un pedazo
        //matrizA.mostrarResumen(10);  //mostramos un segmento de 10x10
        //matrizB.mostrarResumen(10);

        SecuencialIngenuo ingenuo = new SecuencialIngenuo(); //Instanciamos nuestro primer algoritmo
        //Le pasamos el codigo y las matrices a nuestro runner
        BigMatrix resultadoFinal = BenchMarkRunner.evaluarAlgoritmo(ingenuo, matrizA, matrizB);


    }

}