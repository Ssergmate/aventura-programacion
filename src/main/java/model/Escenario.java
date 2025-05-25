package model;

public class Escenario {
    private int id;
    private String nombre;
    private String clima;
    private String momentoDia;
    private String condicionesTerreno; // Ej. "Rocoso", "Llano", "Húmedo"

    public Escenario(int id, String nombre, String clima, String momentoDia, String condicionesTerreno) {
        this.id = id;
        this.nombre = nombre;
        this.clima = clima;
        this.momentoDia = momentoDia;
        this.condicionesTerreno = condicionesTerreno;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClima() {
        return clima;
    }

    public String getMomentoDia() {
        return momentoDia;
    }

    public String getCondicionesTerreno() {
        return condicionesTerreno;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public void setMomentoDia(String momentoDia) {
        this.momentoDia = momentoDia;
    }

    public void setCondicionesTerreno(String condicionesTerreno) {
        this.condicionesTerreno = condicionesTerreno;
    }

    // Métodos para aplicar modificadores de escenario (ej. a batallas)
    public int calcularPenalizacionAtaque() {
        // Ejemplo: Si el terreno es "Húmedo", hay una penalización de 5 al ataque
        if ("Húmedo".equalsIgnoreCase(condicionesTerreno)) {
            return 5;
        }
        return 0;
    }

    public int calcularPenalizacionDefensa() {
        // Ejemplo: Si el clima es "Tormenta", la defensa puede ser más difícil (-10 a la defensa del jugador)
        if ("Tormenta".equalsIgnoreCase(clima)) {
            return 10; // Aumenta el daño recibido
        }
        return 0;
    }
}