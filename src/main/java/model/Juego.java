package model;

// Clase abstracta para representar un minijuego o puzle en el juego
public abstract class Juego {
    protected int idJuego; // ID para la base de datos
    protected String nombre;
    protected String descripcion;
    protected int puntosOtorgados; // Puntos que el jugador gana al superarlo

    public Juego(int idJuego, String nombre, String descripcion, int puntosOtorgados) {
        this.idJuego = idJuego;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puntosOtorgados = puntosOtorgados;
    }

    // Getters
    public int getIdJuego() {
        return idJuego;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPuntosOtorgados() {
        return puntosOtorgados;
    }

    // Setters
    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPuntosOtorgados(int puntosOtorgados) {
        this.puntosOtorgados = puntosOtorgados;
    }

    // Método abstracto para jugar el minijuego.
    // La implementación concreta estará en las subclases específicas de cada minijuego.
    public abstract boolean jugar(); // Devuelve true si el jugador lo supera, false si falla.
}