package model;

public class Ataque {
    private int id;
    private String nombre;
    private int danoBase;
    private String tipoDano; // Ej. "fisico", "magico", "reputacion", "total" (para simplificar)

    public Ataque(int id, String nombre, int danoBase, String tipoDano) {
        this.id = id;
        this.nombre = nombre;
        this.danoBase = danoBase;
        this.tipoDano = tipoDano;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public String getTipoDano() {
        return tipoDano;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDanoBase(int danoBase) {
        this.danoBase = danoBase;
    }

    public void setTipoDano(String tipoDano) {
        this.tipoDano = tipoDano;
    }
}