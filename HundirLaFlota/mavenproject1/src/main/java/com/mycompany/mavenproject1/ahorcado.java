
package com.mycompany.mavenproject1;

import java.util.Scanner; 
public class ahorcado {


    public static void main(String[] args) 
    {
        String palabraSecreta = "programacion"; 
        StringBuilder palabraMostrada = new StringBuilder(" _ ".repeat(palabraSecreta.length())); 
        int intentos = 6; 
        boolean[] letrasAdivinadas = new boolean[palabraSecreta.length()]; 

        Scanner scanner = new Scanner(System.in);  
        
        while (intentos > 0 && palabraMostrada.toString().contains("_")) 
        {
            System.out.println("  ------");
            System.out.println("  |    |");
            System.out.println("  O    |");
            System.out.println(" /|\\   |");
            System.out.println(" / \\   |");
            System.out.println("       |");
            System.out.println("-------");
            System.out.println("Palabra actual: " + palabraMostrada); 
            System.out.println("Tienes " + intentos + " intentos restantes.");
            System.out.print("Ingresa una letra: ");
            char letra = scanner.next().charAt(0); 

            
            boolean letraCorrecta = false; 
            for (int i = 0; i < palabraSecreta.length(); i++) { 

                if (palabraSecreta.charAt(i) == letra && letrasAdivinadas[i]==false) {
                    letrasAdivinadas[i] = true;
                    palabraMostrada.setCharAt(i, letra);  
                    letraCorrecta = true;
                }
            }
            if (letraCorrecta == false) {
                intentos--; 
                System.out.println("Letra incorrecta. Te quedan " + intentos + " intentos.");
            }
            System.out.println(); 
        }

        if (palabraMostrada.toString().equals(palabraSecreta)) 
        {
            System.out.println("¡Felicidades, has adivinado la palabra: " + palabraSecreta + "!");
        } 
        else 
        {
            System.out.println("¡Has perdido! La palabra era: " + palabraSecreta);
        }
    }
}