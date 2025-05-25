package dto;

public class LoginRequestDTO {
    private String nombreUsuario;
    private String contrasena;

    public LoginRequestDTO(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // --- Getters ---
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    // --- Setters (opcional, si necesitas modificar los datos después de la creación) ---
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}