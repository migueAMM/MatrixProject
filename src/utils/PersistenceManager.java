package utils;
import models.BigMatrix;

import java.io.*;

public class PersistenceManager {

    /***
     * Este metodo guarda la matriz con su nombre en archivos .txt
     * @param matriz
     * @param nombreArchivo
     */
    public static void guardarMatriz(BigMatrix matriz , String nombreArchivo){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            int filas = matriz.getFilas();
            int columnas = matriz.getColumnas();
            writer.write(filas + "," + columnas);
            writer.newLine();

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    writer.write(matriz.getValue(i, j) + (j == columnas - 1 ? "" : " "));
                }
                writer.newLine();
            }
            System.out.println("Matriz guardada en: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BigMatrix cargarMatriz(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String[] dimensiones = reader.readLine().split(",");
            int filas = Integer.parseInt(dimensiones[0]);
            int columnas = Integer.parseInt(dimensiones[1]);
            BigMatrix matriz = new BigMatrix(filas, columnas);

            for (int i = 0; i < filas; i++) {
                String[] valores = reader.readLine().split(" ");
                for (int j = 0; j < columnas; j++) {
                    matriz.setValue(i, j, Long.parseLong(valores[j]));
                }
            }
            return matriz;
        } catch (IOException e) {
            System.out.println("No se encontró el archivo, se debe generar uno nuevo.");
            return null;
        }
    }
}
