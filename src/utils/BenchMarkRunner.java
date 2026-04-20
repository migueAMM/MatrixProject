package utils;

import algorithms.MatrixAlgorithms;
import models.BigMatrix;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BenchMarkRunner {

    // Variable para recordar cómo se llama el archivo de esta ejecución en particular
    private static String archivoActual;

    // Este metodo creara el archivo nuevo con los encabezados
    public static void iniciarNuevoReporte() {
        // Obtenemos la fecha y hora actual para el nombre del archivo
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = dtf.format(LocalDateTime.now());
        archivoActual = "resultados_" + timestamp + ".csv";

        // Creamos el archivo y escribimos la primera línea (Cabeceras para Python)
        try (PrintWriter out = new PrintWriter(new FileWriter(archivoActual, false))) {
            out.println("Algoritmo,Tamano,Tiempo_ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Archivo de reporte creado: " + archivoActual + "\n");
    }

    public static BigMatrix evaluarAlgoritmo(MatrixAlgorithms algoritmo, BigMatrix m1, BigMatrix m2) {
        System.out.println("Iniciando: " + algoritmo.getNombre());

        long startTime = System.currentTimeMillis();
        BigMatrix resultado = algoritmo.multiplicar(m1, m2);
        long endTime = System.currentTimeMillis();

        long tiempoTotal = endTime - startTime;
        System.out.println("Tiempo: " + tiempoTotal + " ms\n");

        // Guardamos el resultado de este algoritmo en el archivo actual
        registrarTiempoCSV(algoritmo.getNombre(), m1.getFilas(), tiempoTotal);

        return resultado;
    }

    private static void registrarTiempoCSV(String nombreAlgoritmo, int n, long tiempo) {
        // Por precaución, si no se ha creado el archivo, lo creamos
        if (archivoActual == null) {
            iniciarNuevoReporte();
        }

        // Guardamos el dato (el 'true' significa que no borra lo anterior, sino que añade al final)
        try (PrintWriter out = new PrintWriter(new FileWriter(archivoActual, true))) {
            out.println(nombreAlgoritmo + "," + n + "x" + n + "," + tiempo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}