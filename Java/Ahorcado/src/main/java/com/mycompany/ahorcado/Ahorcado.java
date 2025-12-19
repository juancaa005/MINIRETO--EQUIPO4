/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.ahorcado;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author DAW127
 */
public class Ahorcado {

    public static void main(String[] args) {

        final int INTENTOS_TOTALES = 7;
        int intentos = 0;
        int aciertos = 0;

        Scanner teclado = new Scanner(System.in);

        char resp;
        Random rnd = new Random();

        String arrayPalabras[] = {
            "avion",
            "montana",
            "rio",
            "desierto",
            "galaxia",
            "robot",
            "teclado",
            "pantalla",
            "raton",
            "programa",
            "algoritmo",
            "variable",
            "funcion",
            "clase",
            "objeto",
            "archivo",
            "servidor",
            "red",
            "internet",
            "mensaje",
            "correo",
            "usuario",
            "clave",
            "seguridad",
            "cifrado",
            "sensor",
            "motor",
            "energia",
            "bateria",
            "circuito",
            "volcan",
            "oceano",
            "isla",
            "bosque",
            "selva",
            "pradera",
            "nube",
            "lluvia",
            "viento",
            "tormenta",
            "relampago",
            "trueno",
            "planeta",
            "estrella",
            "cometa",
            "asteroide",
            "satelite",
            "orbita",
            "telescopio",
            "astronauta"
        };

        do {
            int aleatorio = rnd.nextInt(arrayPalabras.length);
            char[] separado = AhorcadoClase.separa(arrayPalabras[aleatorio]);
            char[] copia = AhorcadoClase.separa(arrayPalabras[aleatorio]);

            char[] tusRespuestas = new char[separado.length];
            for (int i = 0; i < tusRespuestas.length; i++) {
                tusRespuestas[i] = '_';
            }

            char[] letrasUsadas = new char[26];
            int numLetrasUsadas = 0;

            System.out.println("\n===== AHORCADO =====");

            while (intentos < INTENTOS_TOTALES && aciertos != tusRespuestas.length) {

                // Palabra oculta
                AhorcadoClase.imprimeOculta(tusRespuestas);

                // Letras usadas 
                System.out.print("   Letras usadas: ");
                for (int i = 0; i < numLetrasUsadas; i++) {
                    System.out.print(letrasUsadas[i] + " ");
                }
                System.out.println();

                System.out.print("\nIntroduce una letra: ");
                resp = teclado.next().toLowerCase().charAt(0);

                boolean repetida = false;
                for (int i = 0; i < numLetrasUsadas; i++) {
                    if (letrasUsadas[i] == resp) {
                        repetida = true;

                    }
                }

                if (repetida) {
                    System.out.println("️  Letra ya usada. No cuenta intento.");
                    continue;
                }

                letrasUsadas[numLetrasUsadas++] = resp;

                boolean acierto = false;
                for (int i = 0; i < separado.length; i++) {
                    if (separado[i] == resp) {
                        tusRespuestas[i] = separado[i];
                        separado[i] = ' ';
                        aciertos++;
                        acierto = true;
                    }
                }

                // Mensaje cuando fallas
                if (!acierto) {
                    intentos++;
                    System.out.println(" Fallaste (" + intentos + "/" + INTENTOS_TOTALES + ")");
                }
            }

            if (aciertos == tusRespuestas.length) {
                System.out.print("\n Has acertado la palabra: ");
                AhorcadoClase.imprimeOculta(tusRespuestas);
            } else {
                System.out.print("\n Has perdido, la palabra era: ");
                for (int i = 0; i < copia.length; i++) {
                    System.out.print(copia[i] + " ");
                }
            }

            intentos = 0;
            aciertos = 0;

            resp = AhorcadoClase.pregunta("\n\n¿Quieres volver a jugar?", teclado);

        } while (resp != 'n');

        teclado.close();
    }
}
