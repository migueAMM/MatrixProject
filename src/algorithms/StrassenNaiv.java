package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 6: Strassen Naiv
 *
 * Divide y vencerás: divide las matrices en 4 submatrices de n/2 x n/2
 * y realiza 7 multiplicaciones recursivas en lugar de 8 (O(n^2.81) vs O(n^3)).
 *
 * Fórmulas de Strassen:
 *   P1 = (A11 + A22) * (B11 + B22)
 *   P2 = (A21 + A22) * B11
 *   P3 = A11 * (B12 - B22)
 *   P4 = A22 * (B21 - B11)
 *   P5 = (A11 + A12) * B22
 *   P6 = (A21 - A11) * (B11 + B12)
 *   P7 = (A12 - A22) * (B21 + B22)
 *
 * La recursión se detiene (caso base) cuando n <= UMBRAL, usando NaivOnArray.
 */
public class StrassenNaiv implements MatrixAlgorithms {

    // Umbral: por debajo de este tamaño usamos multiplicación directa
    private static final int UMBRAL = 64;

    @Override
    public String getNombre() {
        return "6. StrassenNaiv";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        // Ajustar a la siguiente potencia de 2 si es necesario
        int tam = siguientePotenciaDe2(n);
        BigMatrix A = expandir(matrizA, tam);
        BigMatrix B = expandir(matrizB, tam);
        BigMatrix R = strassen(A, B, tam);
        return recortar(R, n);
    }

    private BigMatrix strassen(BigMatrix A, BigMatrix B, int n) {
        // Caso base
        if (n <= UMBRAL) {
            return naiv(A, B, n);
        }

        int mitad = n / 2;

        // Dividir A y B en submatrices
        BigMatrix A11 = submatriz(A, 0,     0,     mitad);
        BigMatrix A12 = submatriz(A, 0,     mitad, mitad);
        BigMatrix A21 = submatriz(A, mitad, 0,     mitad);
        BigMatrix A22 = submatriz(A, mitad, mitad, mitad);

        BigMatrix B11 = submatriz(B, 0,     0,     mitad);
        BigMatrix B12 = submatriz(B, 0,     mitad, mitad);
        BigMatrix B21 = submatriz(B, mitad, 0,     mitad);
        BigMatrix B22 = submatriz(B, mitad, mitad, mitad);

        // 7 productos de Strassen
        BigMatrix P1 = strassen(sumar(A11, A22),    sumar(B11, B22),    mitad);
        BigMatrix P2 = strassen(sumar(A21, A22),    B11,                mitad);
        BigMatrix P3 = strassen(A11,                restar(B12, B22),   mitad);
        BigMatrix P4 = strassen(A22,                restar(B21, B11),   mitad);
        BigMatrix P5 = strassen(sumar(A11, A12),    B22,                mitad);
        BigMatrix P6 = strassen(restar(A21, A11),   sumar(B11, B12),    mitad);
        BigMatrix P7 = strassen(restar(A12, A22),   sumar(B21, B22),    mitad);

        // Combinar los 7 productos en el resultado
        BigMatrix C11 = restar(sumar(sumar(P1, P4), P7), P5);
        BigMatrix C12 = sumar(P3, P5);
        BigMatrix C21 = sumar(P2, P4);
        BigMatrix C22 = restar(sumar(sumar(P1, P3), P6), P2);

        return combinar(C11, C12, C21, C22, n);
    }

    // ── Operaciones auxiliares ────────────────────────────────────────────────

    private BigMatrix naiv(BigMatrix A, BigMatrix B, int n) {
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int k = 0; k < n; k++) {
                long a = A.getValue(i, k);
                for (int j = 0; j < n; j++)
                    R.setValue(i, j, R.getValue(i, j) + a * B.getValue(k, j));
            }
        return R;
    }

    private BigMatrix sumar(BigMatrix A, BigMatrix B) {
        int n = A.getFilas();
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                R.setValue(i, j, A.getValue(i, j) + B.getValue(i, j));
        return R;
    }

    private BigMatrix restar(BigMatrix A, BigMatrix B) {
        int n = A.getFilas();
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                R.setValue(i, j, A.getValue(i, j) - B.getValue(i, j));
        return R;
    }

    private BigMatrix submatriz(BigMatrix M, int filaInicio, int colInicio, int tam) {
        BigMatrix sub = new BigMatrix(tam, tam);
        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                sub.setValue(i, j, M.getValue(filaInicio + i, colInicio + j));
        return sub;
    }

    private BigMatrix combinar(BigMatrix C11, BigMatrix C12, BigMatrix C21, BigMatrix C22, int n) {
        int mitad = n / 2;
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < mitad; i++)
            for (int j = 0; j < mitad; j++) {
                R.setValue(i,          j,          C11.getValue(i, j));
                R.setValue(i,          j + mitad,  C12.getValue(i, j));
                R.setValue(i + mitad,  j,          C21.getValue(i, j));
                R.setValue(i + mitad,  j + mitad,  C22.getValue(i, j));
            }
        return R;
    }

    private BigMatrix expandir(BigMatrix M, int tam) {
        BigMatrix E = new BigMatrix(tam, tam);
        for (int i = 0; i < M.getFilas(); i++)
            for (int j = 0; j < M.getColumnas(); j++)
                E.setValue(i, j, M.getValue(i, j));
        return E;
    }

    private BigMatrix recortar(BigMatrix M, int n) {
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                R.setValue(i, j, M.getValue(i, j));
        return R;
    }

    private int siguientePotenciaDe2(int n) {
        int p = 1;
        while (p < n) p <<= 1;
        return p;
    }
}
