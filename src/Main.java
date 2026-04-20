import algorithms.*;
import models.BigMatrix;
import generators.MatrixGenerator;
import utils.PersistenceManager;
import utils.BenchMarkRunner;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int n = 1024;
        String archivoA = "matrices/matrizA_" + n + ".txt";
        String archivoB = "matrices/matrizB_" + n + ".txt";

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
        // Ejecutar los algoritmos que tenemos en la tabla del enunciado (1-15)
        //Comenzamos con 1.NaivOnArray
        BenchMarkRunner.evaluarAlgoritmo(new NaivOnArray(), matrizA, matrizB);
        //2. NaivLoopUnrollingTwo
        BenchMarkRunner.evaluarAlgoritmo(new NaivLoopUnrollingTwo(), matrizA, matrizB);
        //3. NaivLoopUnrollingFour

        //4. WinogradOriginal

        //5. WinogradScaled

        //6. StrassenNaiv

        //7. StrassenWinograd

        //8. Sequential block

        //9. Parallel Block

        //10. Enhanced Parallel Block

        //11. Sequential block

        //12. Parallel Block

        //13. Enhanced Parallel Block

        //El 14 y 15 estan repetidos en la tabla
        //14. Sequential block

        //15. Parallel Block

        //Aca ejecutamos los algoritmos adicionales que realizamos
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialOptimizado(), matrizA, matrizB);
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialTranspuesta(), matrizA, matrizB);
        BenchMarkRunner.evaluarAlgoritmo(new ParaleloPorFilas(), matrizA, matrizB);

        System.out.println("Pruebas finalizadas. Los tiempos se han guardado en 'resultados_tiempos.csv'.");

        /***
         * En este segmento de codigo estamos automatizando un comando que hara
         * que al ejecutar el codigo salten las graficas generadas de esta
         * ultima ejecucion automaticamente y se muestre en pantalla
         */
        System.out.println("Generando y abriendo gráfica automáticamente...");
        try {
            // "visualization" con 'z' como en tu imagen, y la ruta del script
            ProcessBuilder pb = new ProcessBuilder("python", "visualization/plot_results.py");
            pb.inheritIO();
            Process proceso = pb.start();
            proceso.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}