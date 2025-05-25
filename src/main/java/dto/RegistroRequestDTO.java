package dto;

public class RegistroRequestDTO {
    private String nombreUsuario;
    private String contrasena;
    private String confirmacionContrasena;
    private String personajeElegido; // Para saber qué tipo de Jugador crear

    public RegistroRequestDTO(String nombreUsuario, String contrasena, String confirmacionContrasena, String personajeElegido) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.confirmacionContrasena = confirmacionContrasena;
        this.personajeElegido = personajeElegido;
    }

    // --- Getters ---
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getConfirmacionContrasena() {
        return confirmacionContrasena;
    }

    public String getPersonajeElegido() {
        return personajeElegido;
    }

    // --- Setters (opcional) ---
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setConfirmacionContrasena(String confirmacionContrasena) {
        this.confirmacionContrasena = confirmacionContrasena;
    }

    public void setPersonajeElegido(String personajeElegido) {
        this.personajeElegido = personajeElegido;
    }
}