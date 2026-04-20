import algorithms.*;
import models.BigMatrix;
import generators.MatrixGenerator;
import utils.PersistenceManager;
import utils.BenchMarkRunner;

public class Main {
    public static void main(String[] args) {
        int n = 1024;
        String archivoA = "matrizA_" + n + ".txt";
        String archivoB = "matrizB_" + n + ".txt";

        // Intenta cargar matrices existentes
        BigMatrix matrizA = PersistenceManager.cargarMatriz(archivoA);
        BigMatrix matrizB = PersistenceManager.cargarMatriz(archivoB);

        // Si no existen, generarlas y guardarlas (Solo pasará la primera vez)
        if (matrizA == null || matrizB == null) {
            System.out.println("Generando matrices nuevas... ");
            matrizA = MatrixGenerator.generateRandom(n, n); //Genera numeros de minimo 6 digitos
            matrizB = MatrixGenerator.generateRandom(n, n);

            PersistenceManager.guardarMatriz(matrizA, archivoA);
            PersistenceManager.guardarMatriz(matrizB, archivoB);
        }

        System.out.println("--- Iniciando Pruebas de Rendimiento ---");

        BenchMarkRunner.iniciarNuevoReporte();
        // Ejecutar los algoritmos que tenemos hasta ahora
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialIngenuo(), matrizA, matrizB);
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialOptimizado(), matrizA, matrizB);
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialTranspuesta(), matrizA, matrizB);
        BenchMarkRunner.evaluarAlgoritmo(new ParaleloPorFilas(), matrizA, matrizB);

        System.out.println("Pruebas finalizadas. Los tiempos se han guardado en 'resultados_tiempos.csv'.");
    }
}