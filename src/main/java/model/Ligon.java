package model;

// Clase específica del personaje Ligón
public class Ligon extends Jugador {
    // La "vida" del Ligón es su "reputación", así que lo manejamos con el atributo vida heredado de Personaje
    // y lo nombramos como 'reputacion' para claridad en el contexto del Ligón.

    public Ligon(String nombre, int reputacionInicial, int puntos, String nombreUsuario, String contrasenaHash) {
        super(nombre, reputacionInicial, puntos, nombreUsuario, contrasenaHash);
        // La vida del Personaje base actúa como reputación para el Ligón
        System.out.println("Ligón creado con reputación inicial: " + getVida());
    }

    // Métodos específicos del Ligón
    public void halagar() {
        System.out.println(this.getNombre() + " lanza un cumplido ingenioso.");
        // Lógica para aumentar la reputación
    }

    public void presumir() {
        System.out.println(this.getNombre() + " presume de sus logros.");
        // Lógica para bajar la reputación
    }

    public void serSincero() {
        System.out.println(this.getNombre() + " se muestra vulnerable y sincero.");
        // Lógica para aumentar la reputación
    }

    public void participarMinijuegoSocial(Juego minijuego) {
        System.out.println(this.getNombre() + " participa en el minijuego: " + minijuego.getNombre());
        minijuego.jugar(); // Simula jugar el minijuego, la lógica real estará en JuegoService
    }


    public void aumentarReputacion(int puntos) {
    int nuevaReputacion = getVida() + puntos;
    if (nuevaReputacion < 0) {
        nuevaReputacion = 0;
    }
    setVida(nuevaReputacion);
    }


    public void disminuirReputacion(int puntos) {
    int nuevaReputacion = getVida() - puntos;
    if (nuevaReputacion < 0) {
        nuevaReputacion = 0;
    }
    setVida(nuevaReputacion);
    }




    // Sobreescritura del método atacar para el Ligón (quizás simbólico)
    @Override
    public void atacar() {
        System.out.println(this.getNombre() + " intenta un " + (this.getVida() > 50 ? "ataque de encanto" : "piropo desesperado") + ".");
        // En el caso del Ligón, "atacar" podría ser un fracaso o una interacción social negativa
    }

    // Sobreescritura del método interactuar para el Ligón
    @Override
    public void interactuar() {
        System.out.println(this.getNombre() + " se acerca a una persona atractiva para interactuar.");
    }

    // Método para obtener la reputación (alias de getVida())
    public int getReputacion() {
        return getVida();
    }

    // Método para establecer la reputación (alias de setVida())
    public void setReputacion(int reputacion) {
        setVida(reputacion);
    }
}