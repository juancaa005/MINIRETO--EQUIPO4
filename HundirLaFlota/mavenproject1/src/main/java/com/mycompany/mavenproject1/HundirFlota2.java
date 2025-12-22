package com.mycompany.mavenproject1;

import java.util.Scanner;
import java.util.Random;

public class HundirFlota2 {

    // Constantes
    private static final int TAMANIO_TABLERO = 10;

    // Estados de las casillas
    private static final int AGUA_OCULTA = 0;      // Nadie ha disparado, no hay barco
    private static final int BARCO_OCULTO = 1;     // Hay barco, no disparado
    private static final int AGUA_DESCUBIERTA = 2; // Se disparó y no había barco
    private static final int BARCO_TOCADO = 3;     // Se disparó y había barco
    private static final int BARCO_HUNDIDO = 4;    // Barco completamente hundido

    // Tipos de barcos
    private static final int PORTAAVIONES = 0;
    private static final int ACORAZADO = 1;
    private static final int DESTRUCTOR = 2;
    private static final int SUBMARINO = 3;

    // Configuración de barcos
    private static final int[] TAMANIO_BARCOS = {5, 4, 3, 1}; // Tamaño de cada tipo
    private static final int[] CANTIDAD_BARCOS = {1, 2, 3, 4}; // Cantidad de cada tipo
    private static final String[] NOMBRES_BARCOS = {"Portaaviones", "Acorazado", "Destructor", "Submarino"};

    // Tableros del jugador y máquina
    private int[][] tableroJugador = new int[TAMANIO_TABLERO][TAMANIO_TABLERO];
    private int[][] tableroMaquina = new int[TAMANIO_TABLERO][TAMANIO_TABLERO];

    // Seguimiento de disparos
    private boolean[][] disparosJugador = new boolean[TAMANIO_TABLERO][TAMANIO_TABLERO];
    private boolean[][] disparosMaquina = new boolean[TAMANIO_TABLERO][TAMANIO_TABLERO];

    // Información de barcos
    private int[][][] barcosJugador; //tridimensional [tipo de barco][las casillas que ocupa dicho barco][las cordenadas(fila y columna coordenadas]
    private int[][][] barcosMaquina;// lo mismo que el de arriba pero para los barcos de la maquina contra la que jugamos
    private int[] hundidosJugador = new int[10]; // 10 barcos máximo
    private int[] hundidosMaquina = new int[10];
    private int totalBarcosJugador = 0;
    private int totalBarcosMaquina = 0;

    // OBJETOS
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    // Constructor
    public HundirFlota2() {
        inicializarJuego();
    }

    // Método principal que inicia el juego
    public void iniciarJuego() {
        System.out.println("=== BIENVENIDO A HUNDIR LA FLOTA ===");

        // Colocar barcos del jugador
        colocarBarcosJugador();

        // Colocar barcos de la máquina
        colocarBarcosMaquina();

        // Variables de control del juego
        boolean juegoActivo = true;
        boolean turnoJugador = true; // El jugador comienza

        // Bucle principal del juego
        while (juegoActivo) {
            // Mostrar ambos tableros
            mostrarTablerosLadoALado();

            // Gestionar turnos
            if (turnoJugador) {
                System.out.println("\n=== TU TURNO ===");
                if (realizarDisparoJugador()) {
                    // El Jugador acertó, repite turno
                    System.out.println("¡Acertaste! Tienes otro turno.");
                } else {
                    turnoJugador = false; // Pasa turno a la máquina
                }
            } else {
                System.out.println("\n=== TURNO DE LA MÁQUINA ===");
                if (realizarDisparoMaquina()) {
                    // La Máquina acertó, repite turno
                    System.out.println("La máquina acertó. Le toca otra vez.");
                } else {
                    turnoJugador = true; // Vuelve el turno al jugador
                }
            }

            // Verificar si el juego ha terminado
            if (verificarFinJuego()) {
                juegoActivo = false;
            }

            // Pausa para ver resultados
            System.out.println("\nPresiona Enter para continuar...");
            scanner.nextLine();
        }

        scanner.close();
    }

    // Inicializa los tableros y estructuras de datos
    private void inicializarJuego() {
        // Inicializar los tableros a agua oculta
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                tableroJugador[i][j] = AGUA_OCULTA;
                tableroMaquina[i][j] = AGUA_OCULTA;
                disparosJugador[i][j] = false;
                disparosMaquina[i][j] = false;
            }
        }

        // Inicializar los arrays de barcos 
        barcosJugador = new int[10][][]; // 10 barcos máximo
        barcosMaquina = new int[10][][];
    }

    // Permite al jugador colocar sus barcos manualmente
    private void colocarBarcosJugador() {
    System.out.println("\n=== COLOCACIÓN DE TUS BARCOS ===");
    
    int barcoId = 0;
    
    // Recorrer todos los tipos de barco
    for (int tipo = 0; tipo < TAMANIO_BARCOS.length; tipo++) {
        
        // Colocar la cantidad correspondiente de cada tipo
        for (int i = 0; i < CANTIDAD_BARCOS[tipo]; i++) {
            
            int tamaño = TAMANIO_BARCOS[tipo];
            boolean colocado = false;
            
            // Pedir posición hasta que sea válida
            while (!colocado) {
                
                mostrarTableroJugadorLado(true);
                
                System.out.println("\nColocando " + NOMBRES_BARCOS[tipo]
                        + " (tamaño: " + tamaño + ")");
                
                // Pedir coordenadas al jugador
                System.out.print("Fila inicial (0-9): ");
                int fila = scanner.nextInt();
                
                System.out.print("Columna inicial (0-9): ");
                int columna = scanner.nextInt();
                
                scanner.nextLine(); // Limpiar buffer
                
                // SOLO PREGUNTAR ORIENTACIÓN SI EL BARCO TIENE MÁS DE 1 CASILLA
                boolean horizontal = true; // Valor por defecto (para submarinos)
                
                if (tamaño > 1) {
                    // validar el boolean si introducen otra entrada
                    boolean horizontalValido = false;
                    
                    while (!horizontalValido) {
                        System.out.print("¿Horizontal? (true/false): ");
                        String entrada = scanner.nextLine().toLowerCase();
                        
                        if (entrada.equals("true")) {
                            horizontal = true;
                            horizontalValido = true;
                        } else if (entrada.equals("false")) {
                            horizontal = false;
                            horizontalValido = true;
                        } else {
                            System.out.println(" Error: introduce true o false.");
                        }
                    }
                }
                
                // Verificar si la posición es válida
                if (esColocacionValida(tableroJugador, fila, columna, tamaño, horizontal)) {
                    
                    // Colocar el barco
                    colocarBarco(tableroJugador, barcosJugador, barcoId,
                            fila, columna, tamaño, horizontal);
                    
                    barcoId++;
                    totalBarcosJugador++;
                    colocado = true;
                    
                    System.out.println(" Barco colocado correctamente.");
                } else {
                    System.out.println(" Posición inválida. Intenta de nuevo.");
                }
            }
        }
    }
}
    // Coloca aleatoriamente los barcos de la máquina
    private void colocarBarcosMaquina() {
    int barcoId = 0;
    for (int tipo = 0; tipo < TAMANIO_BARCOS.length; tipo++) {
        for (int i = 0; i < CANTIDAD_BARCOS[tipo]; i++) {
            int tamaño = TAMANIO_BARCOS[tipo];
            boolean colocado = false;
            
            while (!colocado) {
                int fila = random.nextInt(TAMANIO_TABLERO);
                int columna = random.nextInt(TAMANIO_TABLERO);
                
                // PARA SUBMARINOS (tamaño = 1), LA ORIENTACIÓN NO IMPORTA
                boolean horizontal = true; // Por defecto para tamaño 1
                if (tamaño > 1) {
                    horizontal = random.nextBoolean();
                }
                
                if (esColocacionValida(tableroMaquina, fila, columna, tamaño, horizontal)) {
                    colocarBarco(tableroMaquina, barcosMaquina, barcoId, fila, columna, tamaño, horizontal);
                    barcoId++;
                    totalBarcosMaquina++;
                    colocado = true;
                }
            }
        }
    }
    System.out.println("La máquina ha colocado sus barcos.");
}

    // Verifica si una posición para colocar un barco es válida
    private boolean esColocacionValida(int[][] tablero, int fila, int columna,
            int tamaño, boolean horizontal) {
        // Verificar límites del tablero
        if (horizontal) {
            // Verificar que no se salga por la derecha
            if (columna + tamaño > TAMANIO_TABLERO) {
                return false;
            }
            // Verificar que todas las casillas estén libres
            for (int c = columna; c < columna + tamaño; c++) {
                if (tablero[fila][c] != AGUA_OCULTA) {
                    return false;
                }
            }
        } else {
            // Verificar que no se salga por abajo
            if (fila + tamaño > TAMANIO_TABLERO) {
                return false;
            }
            // Verificar que todas las casillas estén libres
            for (int f = fila; f < fila + tamaño; f++) {
                if (tablero[f][columna] != AGUA_OCULTA) {
                    return false;
                }
            }
        }
        return true;
    }

    // Coloca un barco en el tablero y guarda su información
    private void colocarBarco(int[][] tablero, int[][][] barcosArray,
            int barcoId, int fila, int columna,
            int tamaño, boolean horizontal) {
        // Crear array para almacenar las coordenadas de este barco
        barcosArray[barcoId] = new int[tamaño][2];

        // Colocar en el tablero y guardar coordenadas
        if (horizontal) {
            for (int i = 0; i < tamaño; i++) {
                tablero[fila][columna + i] = BARCO_OCULTO;
                // Guardar coordenada
                barcosArray[barcoId][i][0] = fila;
                barcosArray[barcoId][i][1] = columna + i;
            }
        } else {
            for (int i = 0; i < tamaño; i++) {
                tablero[fila + i][columna] = BARCO_OCULTO;
                // Guardar coordenada
                barcosArray[barcoId][i][0] = fila + i;
                barcosArray[barcoId][i][1] = columna;
            }
        }
    }

    // Gestiona el disparo del jugador
    private boolean realizarDisparoJugador() {
        int fila, columna;
        boolean disparoValido = false;

        // Pedir coordenadas hasta que sean válidas
        while (!disparoValido) {
            System.out.print("Fila para disparar (0-9): ");
            fila = scanner.nextInt();
            System.out.print("Columna para disparar (0-9): ");
            columna = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            // Validar coordenadas
            if (fila < 0 || fila >= TAMANIO_TABLERO
                    || columna < 0 || columna >= TAMANIO_TABLERO) {
                System.out.println("Coordenadas fuera de rango. Intenta de nuevo.");
            } else if (disparosJugador[fila][columna]) {
                System.out.println("Ya disparaste aquí. Intenta otra coordenada.");
            } else {
                disparoValido = true;
                disparosJugador[fila][columna] = true;

                // Verificar si hay barco en esa posición
                if (tableroMaquina[fila][columna] == BARCO_OCULTO) {
                    // ¡TOCADO!
                    tableroMaquina[fila][columna] = BARCO_TOCADO;
                    System.out.println("¡TOCADO!");

                    // Verificar si se hundió un barco completo
                    if (verificarYMarcarHundido(tableroMaquina, barcosMaquina, fila, columna, hundidosMaquina)) {
                        System.out.println("¡HUNDIDO!");
                    }
                    return true; // Jugador repite turno por acertar
                } else {
                    // AGUA
                    if (tableroMaquina[fila][columna] == AGUA_OCULTA) {
                        tableroMaquina[fila][columna] = AGUA_DESCUBIERTA;
                    }
                    System.out.println("AGUA");
                    return false; // Turno de la máquina
                }
            }
        }
        return false;
    }

    // Gestiona el disparo de la máquina
    private boolean realizarDisparoMaquina() {
        int fila, columna;
        boolean disparoValido = false;

        // Generar coordenadas aleatorias hasta encontrar una no disparada
        while (!disparoValido) {
            fila = random.nextInt(TAMANIO_TABLERO);
            columna = random.nextInt(TAMANIO_TABLERO);

            if (!disparosMaquina[fila][columna]) {
                disparoValido = true;
                disparosMaquina[fila][columna] = true;

                System.out.println("La máquina dispara a: [" + fila + "," + columna + "]");

                // Verificar si hay barco en esa posición
                if (tableroJugador[fila][columna] == BARCO_OCULTO) {
                    // ¡TOCADO!
                    tableroJugador[fila][columna] = BARCO_TOCADO;
                    System.out.println("¡La máquina te ha TOCADO!");

                    // Verificar si se hundió un barco completo
                    if (verificarYMarcarHundido(tableroJugador, barcosJugador, fila, columna, hundidosJugador)) {
                        System.out.println("¡La máquina ha HUNDIDO uno de tus barcos!");
                    }
                    return true; // Máquina repite turno por acertar
                } else {
                    // AGUA
                    if (tableroJugador[fila][columna] == AGUA_OCULTA) {
                        tableroJugador[fila][columna] = AGUA_DESCUBIERTA;
                    }
                    System.out.println("La máquina ha fallado (AGUA)");
                    return false; // Turno del jugador
                }
            }
        }
        return false;
    }

    // Verifica si un barco ha sido completamente hundido
    private boolean verificarYMarcarHundido(int[][] tablero, int[][][] barcosArray,
            int fila, int columna, int[] hundidos) {
        // Buscar el barco al que pertenece la casilla
        for (int barcoId = 0; barcoId < barcosArray.length; barcoId++) {
            if (barcosArray[barcoId] != null) {
                // Buscar si esta casilla pertenece a este barco
                for (int i = 0; i < barcosArray[barcoId].length; i++) {
                    if (barcosArray[barcoId][i][0] == fila
                            && barcosArray[barcoId][i][1] == columna) {

                        // Verificar si todas las casillas de este barco han sido tocadas
                        boolean completoHundido = true;
                        for (int j = 0; j < barcosArray[barcoId].length; j++) {
                            int f = barcosArray[barcoId][j][0];
                            int c = barcosArray[barcoId][j][1];
                            if (tablero[f][c] != BARCO_TOCADO && tablero[f][c] != BARCO_HUNDIDO) {
                                completoHundido = false;
                                break;
                            }
                        }

                        // Marcar como hundido si todas las casillas están tocadas
                        if (completoHundido && hundidos[barcoId] == 0) {
                            hundidos[barcoId] = 1;
                            // Actualizar todas las casillas del barco a HUNDIDO
                            for (int j = 0; j < barcosArray[barcoId].length; j++) {
                                int f = barcosArray[barcoId][j][0];
                                int c = barcosArray[barcoId][j][1];
                                tablero[f][c] = BARCO_HUNDIDO;
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Verifica si el juego ha terminado (algún jugador ha perdido todos sus barcos)
    private boolean verificarFinJuego() {
        // Verificar si todos los barcos del jugador están hundidos
        boolean jugadorDerrotado = true;
        for (int i = 0; i < totalBarcosJugador; i++) {
            if (hundidosJugador[i] == 0) {
                jugadorDerrotado = false;
                break;
            }
        }

        // Verificar si todos los barcos de la máquina están hundidos
        boolean maquinaDerrotada = true;
        for (int i = 0; i < totalBarcosMaquina; i++) {
            if (hundidosMaquina[i] == 0) {
                maquinaDerrotada = false;
                break;
            }
        }

        // Mostrar resultado del juego
        if (jugadorDerrotado) {
            mostrarTablerosLadoALado();
            System.out.println("\n=== FIN DEL JUEGO ===");
            System.out.println("¡LA MÁQUINA GANA!");
            return true;
        } else if (maquinaDerrotada) {
            mostrarTablerosLadoALado();
            System.out.println("\n=== FIN DEL JUEGO ===");
            System.out.println("¡FELICIDADES! ¡HAS GANADO!");
            return true;
        }

        return false; // El juego continúa
    }

    // Muestra ambos tableros lado a lado
    private void mostrarTablerosLadoALado() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TU TABLERO" + " ".repeat(40) + "TABLERO ENEMIGO");
        System.out.println("=".repeat(70));

        // Encabezados de columnas
        System.out.print("  ");
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
        }
        System.out.print("        "); // 8 espacios de separación
        System.out.print("  ");
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Línea divisoria
        System.out.println("-".repeat(25) + "    " + "-".repeat(25));

        // Filas de ambos tableros
        for (int fila = 0; fila < TAMANIO_TABLERO; fila++) {
            // Tu tablero (izquierda) - muestra todo
            System.out.print(fila + " ");
            for (int col = 0; col < TAMANIO_TABLERO; col++) {
                switch (tableroJugador[fila][col]) {
                    case AGUA_OCULTA:
                        System.out.print("· ");
                        break;
                    case BARCO_OCULTO:
                        System.out.print("· "); // Oculto durante el juego
                        break;
                    case AGUA_DESCUBIERTA:
                        System.out.print("O ");
                        break;
                    case BARCO_TOCADO:
                        System.out.print("X ");
                        break;
                    case BARCO_HUNDIDO:
                        System.out.print("# ");
                        break;
                    default:
                        System.out.print("· ");
                }
            }

            System.out.print("    |    "); // Separador con barras

            // Tablero enemigo (derecha) - solo muestra disparos realizados
            System.out.print(fila + " ");
            for (int col = 0; col < TAMANIO_TABLERO; col++) {
                if (disparosJugador[fila][col]) {
                    // Mostrar resultado del disparo
                    switch (tableroMaquina[fila][col]) {
                        case AGUA_DESCUBIERTA:
                            System.out.print("O ");
                            break;
                        case BARCO_TOCADO:
                            System.out.print("X ");
                            break;
                        case BARCO_HUNDIDO:
                            System.out.print("# ");
                            break;
                        default:
                            System.out.print("O ");
                    }
                } else {
                    // No se ha disparado aquí aún
                    System.out.print("· ");
                }
            }
            System.out.println();
        }

        // Línea divisoria
        System.out.println("-".repeat(25) + "    " + "-".repeat(25));

        // Leyenda
        System.out.println("\nLEYENDA:");
        System.out.println("·: Desconocido  |  O: Agua  |  X: Tocado  |  #: Hundido");
        System.out.println("=".repeat(70));
    }

    // Método para mostrar solo el tablero del jugador (usado durante la colocación)
    private void mostrarTableroJugadorLado(boolean mostrarBarcos) {
        System.out.println("\n=== TU TABLERO ===");
        System.out.print("  ");
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                switch (tableroJugador[i][j]) {
                    case AGUA_OCULTA:
                        System.out.print("· ");
                        break;
                    case BARCO_OCULTO:
                        if (mostrarBarcos) {
                            System.out.print("B "); // Muestra los barcos durante colocación
                        } else {
                            System.out.print("· ");
                        }
                        break;
                    case AGUA_DESCUBIERTA:
                        System.out.print("O ");
                        break;
                    case BARCO_TOCADO:
                        System.out.print("X ");
                        break;
                    case BARCO_HUNDIDO:
                        System.out.print("# ");
                        break;
                    default:
                        System.out.print("· ");
                }
            }
            System.out.println();
        }
    }
}
