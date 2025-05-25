package model;

// Clase específica del personaje Granjero
public class Granjero extends Jugador {
    private String herramientaPrincipal; // Ejemplo: "Azada"
    private boolean tieneAzada; // Para la lógica de la azada con el vecino

    public Granjero(String nombre, int vida, int puntos, String nombreUsuario, String contrasenaHash) {
        super(nombre, vida, puntos, nombreUsuario, contrasenaHash);
        this.herramientaPrincipal = "Azada"; // Valor por defecto
        this.tieneAzada = true; // Empieza con ella, pero puede perderla o recuperarla
    }

    // Getters y Setters específicos
    public String getHerramientaPrincipal() {
        return herramientaPrincipal;
    }

    public void setHerramientaPrincipal(String herramientaPrincipal) {
        this.herramientaPrincipal = herramientaPrincipal;
    }

    public boolean isTieneAzada() {
        return tieneAzada;
    }

    public void setTieneAzada(boolean tieneAzada) {
        this.tieneAzada = tieneAzada;
    }

    // Métodos específicos del Granjero
    public void plantarCultivo(String cultivo) {
        if (tieneAzada) {
            System.out.println(this.getNombre() + " está plantando " + cultivo + " con su " + herramientaPrincipal + ".");
            // Aquí iría la lógica detallada de plantar (arar, sembrar, regar)
        } else {
            System.out.println(this.getNombre() + " no tiene la azada para plantar.");
        }
    }

    public void cosecharCultivo(String cultivo) {
        System.out.println(this.getNombre() + " está cosechando " + cultivo + ".");
        // Lógica para añadir dinero o ítems al inventario
    }

    // Sobreescritura del método atacar para el Granjero
    @Override
    public void atacar() {
        System.out.println(this.getNombre() + " usa su " + herramientaPrincipal + " para atacar.");
        // Aquí se llamaría a la lógica de ataque específica (ej. Golpear con Azada)
    }

    // Sobreescritura del método interactuar para el Granjero
    @Override
    public void interactuar() {
        System.out.println(this.getNombre() + " está interactuando con los animales de la granja.");
    }
}