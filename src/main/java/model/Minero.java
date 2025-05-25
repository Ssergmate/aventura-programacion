package model;

public class Minero extends Jugador {
    private String herramientaPrincipal; // Ejemplo: "Pico"
    private int nivelMinaActual;

    public Minero(String nombre, int vida, int puntos, String nombreUsuario, String contrasenaHash) {
        super(nombre, vida, puntos, nombreUsuario, contrasenaHash);
        this.herramientaPrincipal = "Pico";
        this.nivelMinaActual = 1; // Empieza en el nivel 1
    }

    public String getHerramientaPrincipal() {
        return herramientaPrincipal;
    }

    public void setHerramientaPrincipal(String herramientaPrincipal) {
        this.herramientaPrincipal = herramientaPrincipal;
    }

    public int getNivelMinaActual() {
        return nivelMinaActual;
    }

    public void setNivelMinaActual(int nivelMinaActual) {
        this.nivelMinaActual = nivelMinaActual;
    }

    public void excavar() {
        System.out.println(this.getNombre() + " está excavando con su " + this.herramientaPrincipal + ".");
        // Lógica para encontrar recursos o avanzar de nivel
    }

    public void explorarMina() {
        System.out.println(this.getNombre() + " está explorando el nivel " + this.nivelMinaActual + " de la mina.");
        // Lógica para encontrar eventos, enemigos, etc.
    }

    @Override
    public void atacar() {
        System.out.println(this.getNombre() + " usa su " + this.herramientaPrincipal + " para atacar en la mina.");
    }

    @Override
    public void interactuar() {
        System.out.println(this.getNombre() + " se encuentra un vendedor misterioso en la mina.");
    }
}