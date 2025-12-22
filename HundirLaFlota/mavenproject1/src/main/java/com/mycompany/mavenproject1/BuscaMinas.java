/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author DAW116
 */
public class BuscaMinas {

        public static void main(String[] args) {

            final int TAM = 5;
            final int MINAS = 5;

            char[][] tablero = new char[TAM][TAM];
            boolean[][] minas = new boolean[TAM][TAM];
            boolean[][] descubierto = new boolean[TAM][TAM];

            inicializarTablero(tablero);
            colocarMinas(minas, MINAS);
            contarMinas(tablero, minas);

            Scanner sc = new Scanner(System.in);
            boolean gameOver = false;

            while (!gameOver) {
                mostrarTablero(tablero, descubierto);

                System.out.print("Fila (0-4): ");
                int fila = sc.nextInt();
                System.out.print("Columna (0-4): ");
                int col = sc.nextInt();

                if (fila < 0 || fila >= TAM || col < 0 || col >= TAM) {
                    System.out.println("Posición inválida");
                    continue;
                }

                descubierto[fila][col] = true;

                if (minas[fila][col]) {
                    System.out.println("💥 BOOM! Has pisado una mina");
                    mostrarMinas(minas);
                    gameOver = true;
                } else if (haGanado(descubierto, minas)) {
                    System.out.println("🎉 ¡Has ganado!");
                    mostrarTablero(tablero, descubierto);
                    gameOver = true;
                }
            }

            sc.close();
        }

        // Inicializa el tablero con '*'
        static void inicializarTablero(char[][] tablero) {
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    tablero[i][j] = '*';
                }
            }
        }

        // Coloca minas aleatoriamente
        static void colocarMinas(boolean[][] minas, int numMinas) {
            Random r = new Random();
            int colocadas = 0;

            while (colocadas < numMinas) {
                int fila = r.nextInt(minas.length);
                int col = r.nextInt(minas.length);

                if (!minas[fila][col]) {
                    minas[fila][col] = true;
                    colocadas++;
                }
            }
        }

        // Cuenta minas cercanas
        static void contarMinas(char[][] tablero, boolean[][] minas) {
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    if (!minas[i][j]) {
                        int contador = 0;

                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                int nf = i + x;
                                int nc = j + y;

                                if (nf >= 0 && nf < tablero.length
                                        && nc >= 0 && nc < tablero.length
                                        && minas[nf][nc]) {
                                    contador++;
                                }
                            }
                        }
                        tablero[i][j] = (char) (contador + '0');
                    }
                }
            }
        }

        // Muestra el tablero al jugador
        static void mostrarTablero(char[][] tablero, boolean[][] descubierto) {
            System.out.println("\nTABLERO:");
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    if (descubierto[i][j]) {
                        System.out.print(tablero[i][j] + " ");
                    } else {
                        System.out.print("* ");
                    }
                }
                System.out.println();
            }
        }

        // Muestra las minas al perder
        static void mostrarMinas(boolean[][] minas) {
            System.out.println("\nMINAS:");
            for (int i = 0; i < minas.length; i++) {
                for (int j = 0; j < minas[i].length; j++) {
                    if (minas[i][j]) {
                        System.out.print("X ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
        }

        // Comprueba si ha ganado
        static boolean haGanado(boolean[][] descubierto, boolean[][] minas) {
            for (int i = 0; i < descubierto.length; i++) {
                for (int j = 0; j < descubierto[i].length; j++) {
                    if (!minas[i][j] && !descubierto[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }


