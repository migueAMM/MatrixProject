package models;

public class BigMatrix {

    private final int filas; //Filas
    private final int columnas; // columnas
    private final long[][] matriz;

    public BigMatrix(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new long[filas][columnas]; //Creamos el arreglo de datos primitivos por lo cual se llena con 0

    }

//Generamos los getters
    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public long getValue(int fila, int columna){
        return matriz[fila][columna];
    }
    public void setValue(int fila, int columna, long valor){
        matriz[fila][columna] = valor;
    }

    public void sumatoriaValor(int fila, int columna, long valor){
        matriz[fila][columna] = valor;
    }

    public void mostrarResumen (int limite){

        int filasAMostrar = Math.min(filas, limite);
        int columnasAMostrar = Math.min(columnas, limite);

        for (int i = 0 ; i < filasAMostrar ; i++){
            for (int j = 0 ; j < columnasAMostrar ; j++){
                System.out.println("[" + matriz[i][j] + "]");

            }
            System.out.println(); //Hacemos un salto de linea
        }
        if (filas > limite || columnas > limite){

            System.out.println("... y " + (filas - limite) + " filas/columnas más (ocultas por tamaño).");
        }
    }




}
