package com.mycompany.ahorcado;

import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author DAW127
 */
public class AhorcadoClase {
    // Imprime una pregunta para comenzar una nueva partida
    public static char pregunta(String men, Scanner teclado) {
        char resp;
        System.out.println(men + " (s/n)");
        resp = teclado.next().toLowerCase().charAt(0);
        while (resp != 's' && resp != 'n') {
            System.out.println("Error! solo se admite S o N");
            resp = teclado.next().toLowerCase().charAt(0);
        }
        return resp;
    }

    // Esto separa el String en un array de caracteres
    public static char[] separa(String palAzar) {
        char[] letras;
        letras = new char[palAzar.length()];
        for (int i = 0; i < palAzar.length(); i++) {
            letras[i] = palAzar.charAt(i);
        }
        return letras;
    }

    // Esto imprime la palabra con espacios
    public static void imprimeOculta(char[] tusRespuestas) {

        for (int i = 0; i < tusRespuestas.length; i++) {
            System.out.print(tusRespuestas[i] + " ");
        }
    }

}
