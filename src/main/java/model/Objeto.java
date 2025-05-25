package model;

public class Objeto {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio; // Precio en la tienda
    private String tipo; // Ej. "Cultivo", "Herramienta", "Coleccionable", "Transporte", "Romance"

    public Objeto(int id, String nombre, String descripcion, int precio, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getTipo() {
        return tipo;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}