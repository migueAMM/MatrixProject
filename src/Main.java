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
            matrizA = MatrixGenerator.generateRandom(n, n);
            matrizB = MatrixGenerator.generateRandom(n, n);
            PersistenceManager.guardarMatriz(matrizA, archivoA);
            PersistenceManager.guardarMatriz(matrizB, archivoB);
        }

        System.out.println("--- Iniciando Pruebas de Rendimiento ---");

        BenchMarkRunner.iniciarNuevoReporte();

        // ── Algoritmos 1–7: Fuerza bruta, Loop Unrolling, Winograd, Strassen ──

        // 1. NaivOnArray
        BenchMarkRunner.evaluarAlgoritmo(new NaivOnArray(), matrizA, matrizB);

        // 2. NaivLoopUnrollingTwo
        BenchMarkRunner.evaluarAlgoritmo(new NaivLoopUnrollingTwo(), matrizA, matrizB);

        // 3. NaivLoopUnrollingFour
        BenchMarkRunner.evaluarAlgoritmo(new NaivLoopUnrollingFour(), matrizA, matrizB);

        // 4. WinogradOriginal
        BenchMarkRunner.evaluarAlgoritmo(new WinogradOriginal(), matrizA, matrizB);

        // 5. WinogradScaled
        BenchMarkRunner.evaluarAlgoritmo(new WinogradScaled(), matrizA, matrizB);

        // 6. StrassenNaiv
        BenchMarkRunner.evaluarAlgoritmo(new StrassenNaiv(), matrizA, matrizB);

        // 7. StrassenWinograd
        BenchMarkRunner.evaluarAlgoritmo(new StrassenWinograd(), matrizA, matrizB);

        // ── Algoritmos 8–10: III Row x Column (Sequential, Parallel, Enhanced) ──

        // 8. III.3 Sequential Block
        BenchMarkRunner.evaluarAlgoritmo(new SequentialBlockIII(), matrizA, matrizB);

        // 9. III.4 Parallel Block
        BenchMarkRunner.evaluarAlgoritmo(new ParallelBlockIII(), matrizA, matrizB);

        // 10. III.5 Enhanced Parallel Block
        BenchMarkRunner.evaluarAlgoritmo(new EnhancedParallelBlockIII(), matrizA, matrizB);

        // ── Algoritmos 11–13: IV Row x Row (Sequential, Parallel, Enhanced) ──

        // 11. IV.3 Sequential Block
        BenchMarkRunner.evaluarAlgoritmo(new SequentialBlockIV(), matrizA, matrizB);

        // 12. IV.4 Parallel Block
        BenchMarkRunner.evaluarAlgoritmo(new ParallelBlockIV(), matrizA, matrizB);

        // 13. IV.5 Enhanced Parallel Block
        BenchMarkRunner.evaluarAlgoritmo(new EnhancedParallelBlockIV(), matrizA, matrizB);

        // ── Algoritmos 14–15: V Column x Column (Sequential, Parallel) ──

        // 14. V.3 Sequential Block
        BenchMarkRunner.evaluarAlgoritmo(new SequentialBlockV(), matrizA, matrizB);

        // 15. V.4 Parallel Block
        BenchMarkRunner.evaluarAlgoritmo(new ParallelBlockV(), matrizA, matrizB);

        // ── Algoritmos adicionales propios ────────────────────────────────────

        // 16. Paralelo por filas (hilos nativos)
        BenchMarkRunner.evaluarAlgoritmo(new ParaleloPorFilas(), matrizA, matrizB);

        // 17. Secuencial Optimizado (orden i,k,j)
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialOptimizado(), matrizA, matrizB);

        // 18. Secuencial con transpuesta de B
        BenchMarkRunner.evaluarAlgoritmo(new SecuencialTranspuesta(), matrizA, matrizB);

        System.out.println("Pruebas finalizadas. Los tiempos se han guardado en 'resultados_tiempos.csv'.");

        // Generar y abrir gráfica automáticamente
        System.out.println("Generando y abriendo gráfica automáticamente...");
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "visualization/plot_results.py");
            pb.inheritIO();
            Process proceso = pb.start();
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
