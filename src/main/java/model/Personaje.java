package model;

// Clase abstracta base para todos los personajes del juego
public abstract class Personaje {
    protected String nombre;
    protected int vida; // Representa vida para enemigos/granjero/minero, y reputación para el ligón
    protected int puntos; // Puntos que el personaje acumula

    public Personaje(String nombre, int vida, int puntos) {
        this.nombre = nombre;
        this.vida = vida;
        this.puntos = puntos;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getPuntos() {
        return puntos;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setVida(int vida) {
        // Asegurarse de que la vida no baje de 0
        this.vida = Math.max(0, vida);
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    // Métodos abstractos que cada subclase deberá implementar
    public abstract void atacar(); // Podría recibir un objetivo

    // Método para recibir daño (común a todos los personajes)
    public void recibirDano(int cantidadDano) {
        this.vida -= cantidadDano;
        if (this.vida < 0) {
            this.vida = 0;
        }
        System.out.println(this.nombre + " recibe " + cantidadDano + " de daño. Vida restante: " + this.vida);
    }

    // Otras interacciones genéricas
    public abstract void interactuar();
}