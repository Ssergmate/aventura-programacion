package model;

import java.util.List;
import java.util.ArrayList; // Para la lista de ataques

// Clase para los personajes enemigos
public class Enemigo extends Personaje {
    private String especie;
    private List<Ataque> ataquesDisponibles;

    public Enemigo(String nombre, int vida, String especie) {
        super(nombre, vida, 0); // Los enemigos no acumulan puntos para el jugador, o no se muestran
        this.especie = especie;
        this.ataquesDisponibles = new ArrayList<>();
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public List<Ataque> getAtaquesDisponibles() {
        return ataquesDisponibles;
    }

    public void agregarAtaque(Ataque ataque) {
        this.ataquesDisponibles.add(ataque);
    }

    // Sobreescritura del método atacar (para que el enemigo elija un ataque)
    @Override
    public void atacar() {
        // La lógica real de selección de ataque y aplicación de daño estará en BatallaService
        System.out.println(this.nombre + " realiza un ataque aleatorio.");
    }

    @Override
    public void interactuar() {
        System.out.println(this.nombre + " gruñe amenazadoramente.");
    }
}