package model;

import java.util.ArrayList;
import java.util.List;

// Clase base para los personajes jugables
public class Jugador extends Personaje {
    private String nombreUsuario; // Para el login/registro
    private String contrasenaHash; // Contraseña encriptada
    private int puntuacionTotal; // Puntuación global del jugador
    private List<Objeto> inventario; // Lista de objetos en el inventario del jugador
    private List<String> finalesDesbloqueados; // Nombres de los finales conseguidos por el jugador
    private int vidaMaxima; // Vida máxima del jugador, se puede modificar según el personaje

    public Jugador(String nombre, int vida, int puntos, String nombreUsuario, String contrasenaHash) {
        super(nombre, vida, puntos); // Llama al constructor de Personaje
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.puntuacionTotal = 0; // Se inicializa en 0 al crear el jugador
        this.inventario = new ArrayList<>();
        this.finalesDesbloqueados = new ArrayList<>();
    }


    // Getters específicos de Jugador
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public List<Objeto> getInventario() {
        return inventario;
    }

    public List<String> getFinalesDesbloqueados() {
        return finalesDesbloqueados;
    }

    // Setters específicos de Jugador
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public void setPuntuacionTotal(int puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public void agregarObjeto(Objeto objeto) {
        this.inventario.add(objeto);
        System.out.println(this.nombre + " ha añadido " + objeto.getNombre() + " a su inventario.");
    }

    public void eliminarObjeto(Objeto objeto) {
        this.inventario.remove(objeto);
        System.out.println(this.nombre + " ha usado/eliminado " + objeto.getNombre() + " de su inventario.");
    }

    public void agregarPuntos(int puntosGanados) {
        this.puntuacionTotal += puntosGanados;
        System.out.println(this.nombre + " ha ganado " + puntosGanados + " puntos. Puntuación total: " + this.puntuacionTotal);
    }

    public void desbloquearFinal(String finalNombre) {
        if (!finalesDesbloqueados.contains(finalNombre)) {
            finalesDesbloqueados.add(finalNombre);
            System.out.println("¡" + this.nombre + " ha desbloqueado el final: " + finalNombre + "!");
        }
    }

    public int getVidaMaxima() {
    return vidaMaxima;
    }

    // Implementación de métodos abstractos de Personaje (genéricos para Jugador)
    @Override
    public void atacar() {
        System.out.println(this.nombre + " realiza un ataque genérico.");
        // Esta implementación será sobreescrita por Granjero, Minero, Ligón
    }

    @Override
    public void interactuar() {
        System.out.println(this.nombre + " interactúa con el entorno.");
        // Esta implementación será sobreescrita por los personajes específicos
    }
}