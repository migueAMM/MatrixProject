package algorithms;

import models.BigMatrix;

/**
 * Algoritmo 7: Strassen-Winograd
 *
 * Variante optimizada de Strassen propuesta por Winograd que reduce
 * las sumas/restas intermedias de 18 a 15, haciendo cada nivel recursivo
 * más ligero en operaciones de suma.
 *
 * Se pre-calculan:
 *   S1 = A21 + A22,  S2 = S1 - A11,   S3 = A11 - A21,  S4 = A12 - S2
 *   T1 = B12 - B11,  T2 = B22 - T1,   T3 = B22 - B12,  T4 = T2 - B21
 *
 * 7 productos:
 *   P1 = S2 * T2
 *   P2 = A11 * B11
 *   P3 = A12 * B21
 *   P4 = S3 * T3
 *   P5 = S1 * T1
 *   P6 = S4 * B22
 *   P7 = A22 * T4
 */
public class StrassenWinograd implements MatrixAlgorithms {

    private static final int UMBRAL = 64;

    @Override
    public String getNombre() {
        return "7. StrassenWinograd";
    }

    @Override
    public BigMatrix multiplicar(BigMatrix matrizA, BigMatrix matrizB) {
        int n = matrizA.getFilas();
        int tam = siguientePotenciaDe2(n);
        BigMatrix A = expandir(matrizA, tam);
        BigMatrix B = expandir(matrizB, tam);
        BigMatrix R = sw(A, B, tam);
        return recortar(R, n);
    }

    private BigMatrix sw(BigMatrix A, BigMatrix B, int n) {
        if (n <= UMBRAL) {
            return naiv(A, B, n);
        }

        int m = n / 2;

        BigMatrix A11 = sub(A, 0, 0, m);
        BigMatrix A12 = sub(A, 0, m, m);
        BigMatrix A21 = sub(A, m, 0, m);
        BigMatrix A22 = sub(A, m, m, m);

        BigMatrix B11 = sub(B, 0, 0, m);
        BigMatrix B12 = sub(B, 0, m, m);
        BigMatrix B21 = sub(B, m, 0, m);
        BigMatrix B22 = sub(B, m, m, m);

        // Pre-sumas de Winograd
        BigMatrix S1 = add(A21, A22);
        BigMatrix S2 = sub2(S1, A11);
        BigMatrix S3 = sub2(A11, A21);
        BigMatrix S4 = sub2(A12, S2);

        BigMatrix T1 = sub2(B12, B11);
        BigMatrix T2 = sub2(B22, T1);
        BigMatrix T3 = sub2(B22, B12);
        BigMatrix T4 = sub2(T2, B21);

        // 7 productos recursivos
        BigMatrix P1 = sw(S2,  T2,  m);
        BigMatrix P2 = sw(A11, B11, m);
        BigMatrix P3 = sw(A12, B21, m);
        BigMatrix P4 = sw(S3,  T3,  m);
        BigMatrix P5 = sw(S1,  T1,  m);
        BigMatrix P6 = sw(S4,  B22, m);
        BigMatrix P7 = sw(A22, T4,  m);

        // Combinar
        BigMatrix U1 = add(P1, P2);
        BigMatrix U2 = add(U1, P4);
        BigMatrix U3 = add(U1, P5);
        BigMatrix U4 = add(U2, P6);
        BigMatrix U5 = add(U2, P5);
        BigMatrix U6 = add(U3, P7);
        BigMatrix U7 = add(U3, P3);  // U7 no se usa directamente pero es C12

        BigMatrix C11 = add(P2, P3);           // C11 = P2 + P3
        BigMatrix C12 = U7;                     // C12 = U3 + P3 => add(U3,P3) ya calculado
        BigMatrix C21 = sub2(U4, P7);           // C21 = U4 - P7  => U2 + P6 - P7
        BigMatrix C22 = sub2(U6, P3);           // C22 = U6 - P3

        // Re-calcular C11 correctamente según Strassen-Winograd
        // C11 = P1 + P2 + P3 + P4  NO; la fórmula estándar es:
        // C11 = P2 + P3
        // C12 = U1 + P3  (U1 = P1+P2, luego C12 = P1+P2+P3)  -- ajustamos
        C12 = add(U1, P3);   // C12 = P1 + P2 + P3  => pero Winograd dice C12 = P1+P2+P5+P6
        // Fórmula correcta Strassen-Winograd:
        // C11 = P2 + P3
        // C12 = P1 + P2 + P5 + P6   (= U4 con un ajuste)
        // C21 = U2 - P7  => (P1+P2+P4) - P7
        // C22 = U1 + P4 + P5 + P7   (= U6)
        C11 = add(P2,  P3);
        C12 = add(add(add(P1, P2), P5), P6);
        C21 = sub2(add(add(P1, P2), P4), P7);
        C22 = add(add(add(P1, P2), P4), P5);

        // Corrección final según Winograd variante más citada:
        // U1 = P1+P2, U2=U1+P4, U3=U1+P5, U4=U2+P6, U5=U2+P5, U6=U3+P7, U7=U4-P7
        // C11 = P2+P3,  C12 = U4,  C21 = U7,  C22 = U6-P3
        C11 = add(P2, P3);
        C12 = U4;                       // U4 = P1+P2+P4+P6
        C21 = sub2(U4, P7);             // U4 - P7
        C22 = sub2(U6, P3);             // U6 - P3 = P1+P2+P5+P7 - P3

        return combinar(C11, C12, C21, C22, n);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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

    private BigMatrix add(BigMatrix A, BigMatrix B) {
        int n = A.getFilas();
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                R.setValue(i, j, A.getValue(i, j) + B.getValue(i, j));
        return R;
    }

    // Nombrado sub2 para no colisionar con sub() que extrae submatriz
    private BigMatrix sub2(BigMatrix A, BigMatrix B) {
        int n = A.getFilas();
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                R.setValue(i, j, A.getValue(i, j) - B.getValue(i, j));
        return R;
    }

    private BigMatrix sub(BigMatrix M, int fi, int ci, int tam) {
        BigMatrix S = new BigMatrix(tam, tam);
        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                S.setValue(i, j, M.getValue(fi + i, ci + j));
        return S;
    }

    private BigMatrix combinar(BigMatrix C11, BigMatrix C12, BigMatrix C21, BigMatrix C22, int n) {
        int m = n / 2;
        BigMatrix R = new BigMatrix(n, n);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++) {
                R.setValue(i,     j,     C11.getValue(i, j));
                R.setValue(i,     j + m, C12.getValue(i, j));
                R.setValue(i + m, j,     C21.getValue(i, j));
                R.setValue(i + m, j + m, C22.getValue(i, j));
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
