package service;

import model.Jugador; // Importa tu clase Jugador
import model.Enemigo; // Importa tu clase Enemigo
import model.Ataque; // Importa tu clase Ataque
import model.Escenario; // Importa tu clase Escenario
import model.Granjero; // Importa Granjero para instanceof
import model.Minero;   // Importa Minero para instanceof
import model.Ligon;    // Importa Ligon para instanceof


import java.util.Random;
import java.util.Scanner;

public class BatallaService {
    private Random random = new Random();
    private Scanner sc;

    public BatallaService(Scanner sc) { // Constructor para inyectar el Scanner
        this.sc = sc;
    }

    /**
     * Inicia una secuencia de batalla por turnos entre un jugador y un enemigo.
     *
     * @param jugador El objeto Jugador que participa en la batalla.
     * @param enemigo El objeto Enemigo al que se enfrenta el jugador.
     * @param escenario El objeto Escenario donde tiene lugar la batalla (para modificadores).
     * @return true si el jugador gana la batalla, false si el jugador pierde.
     */
    public boolean iniciarBatalla(Jugador jugador, Enemigo enemigo, Escenario escenario) {
        System.out.println("\n--- ¡BATALLA INICIADA! ---");
        System.out.println(jugador.getNombre() + " (Vida: " + jugador.getVida() + ") vs. " +
                           enemigo.getNombre() + " (Vida: " + enemigo.getVida() + ")");
        System.out.println("Escenario: " + escenario.getNombre() + " (Terreno: " + escenario.getCondicionesTerreno() + ")");

        while (jugador.getVida() > 0 && enemigo.getVida() > 0) {
            // Turno del Jugador
            System.out.println("\n--- Turno de " + jugador.getNombre() + " ---");
            System.out.println("Vida de " + jugador.getNombre() + ": " + jugador.getVida());
            System.out.println("Vida de " + enemigo.getNombre() + ": " + enemigo.getVida());

            System.out.println("Elige tu ataque:");
            // Mostrar ataques específicos del personaje
            if (jugador instanceof Granjero) {
                System.out.println("1. Golpe de Azada (Daño base: 30)");
                System.out.println("2. Semillazos (Daño base: 20)");
                System.out.println("3. Dar rábano (Cura al enemigo -20)"); // Daño negativo = curación
            } else if (jugador instanceof Minero) {
                System.out.println("1. Martillo pesado (Daño base: 25)");
                System.out.println("2. Explotar dinamita (Daño base: 40)");
                System.out.println("3. Luz cegadora de la linterna (Daño base: 10)");
            } else if (jugador instanceof Ligon) {
                 System.out.println("1. Lanzar cumplido (Daño base: 5)");
                 System.out.println("2. Ignorar (Daño base: 0)");
            } else {
                System.out.println("1. Ataque normal (Daño base: 15)"); // Ataque genérico si no se reconoce
            }

            int opcionAtaque = sc.nextInt();
            sc.nextLine(); // Consumir el salto de línea

            int danoJugador = 0;
            String nombreAtaqueJugador = "Ataque Genérico";

            // Lógica para determinar el daño basado en el personaje y la opción
            if (jugador instanceof Granjero) {
                switch (opcionAtaque) {
                    case 1: danoJugador = 30; nombreAtaqueJugador = "Golpe de Azada"; break;
                    case 2: danoJugador = 20; nombreAtaqueJugador = "Semillazos"; break;
                    case 3: danoJugador = -20; nombreAtaqueJugador = "Dar rábano"; break;
                    default: System.out.println("Opción inválida, turno perdido."); danoJugador = 0; break;
                }
            } else if (jugador instanceof Minero) {
                switch (opcionAtaque) {
                    case 1: danoJugador = 25; nombreAtaqueJugador = "Martillo pesado"; break;
                    case 2: danoJugador = 40; nombreAtaqueJugador = "Explotar dinamita"; break;
                    case 3: danoJugador = 10; nombreAtaqueJugador = "Luz cegadora de la linterna"; break;
                    default: System.out.println("Opción inválida, turno perdido."); danoJugador = 0; break;
                }
            } else if (jugador instanceof Ligon) {
                switch (opcionAtaque) {
                    case 1: danoJugador = 5; nombreAtaqueJugador = "Lanzar cumplido"; break;
                    case 2: danoJugador = 0; nombreAtaqueJugador = "Ignorar"; // No hace daño, afecta reputación
                    default: System.out.println("Opción inválida, turno perdido."); danoJugador = 0; break;
                }
            } else {
                danoJugador = 15;
            }

            // Aplicar penalizaciones del escenario al daño del jugador
            danoJugador -= escenario.calcularPenalizacionAtaque();
            danoJugador = Math.max(0, danoJugador);

            System.out.println(jugador.getNombre() + " usa " + nombreAtaqueJugador + " y causa " + danoJugador + " de daño.");
            enemigo.recibirDano(danoJugador);
            System.out.println(enemigo.getNombre() + " vida restante: " + enemigo.getVida());

            if (enemigo.getVida() <= 0) {
                System.out.println("¡" + enemigo.getNombre() + " ha sido derrotado!");
                return true;
            }

            // Turno del Enemigo
            System.out.println("\n--- Turno de " + enemigo.getNombre() + " ---");
            if (enemigo.getAtaquesDisponibles().isEmpty()) {
                System.out.println(enemigo.getNombre() + " no tiene ataques definidos. No puede atacar.");
                // Si el enemigo no tiene ataques definidos, le damos uno por defecto para evitar NullPointerException
                enemigo.agregarAtaque(new Ataque(0, "Ataque Básico", 10, "total"));
            }

            Ataque ataqueEnemigo = enemigo.getAtaquesDisponibles().get(random.nextInt(enemigo.getAtaquesDisponibles().size()));
            int danoEnemigo = ataqueEnemigo.getDanoBase();

            // Aplicar penalizaciones del escenario a la defensa del jugador
            danoEnemigo += escenario.calcularPenalizacionDefensa();
            danoEnemigo = Math.max(0, danoEnemigo);

            System.out.println(enemigo.getNombre() + " usa " + ataqueEnemigo.getNombre() + " y causa " + danoEnemigo + " de daño.");
            jugador.recibirDano(danoEnemigo);
            System.out.println(jugador.getNombre() + " vida restante: " + jugador.getVida());

            if (jugador.getVida() <= 0) {
                System.out.println("¡" + jugador.getNombre() + " ha sido derrotado!");
                return false;
            }
        }
        return false;
    }
}