package service;

import dto.LoginRequestDTO;
import dto.RegistroRequestDTO;
import model.Jugador;
import model.Granjero;
import model.Minero;
import model.Ligon;
import repository.JugadorRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class UsuarioService {

    private JugadorRepository jugadorRepository;

    public UsuarioService() {
        this.jugadorRepository = new JugadorRepository();
    }

    public Jugador login(LoginRequestDTO loginRequest) {
    String nombreUsuario = loginRequest.getNombreUsuario();
    String contrasena = loginRequest.getContrasena();
    Jugador jugador = jugadorRepository.buscarPorNombreUsuario(nombreUsuario);
    if (jugador != null && jugador.getContrasenaHash().equals(hashContrasena(contrasena))) {
        return jugador;
    }
    return null;
}

public boolean registrarUsuario(RegistroRequestDTO registroRequest) {
    String nombreUsuario = registroRequest.getNombreUsuario();
    String contrasena = registroRequest.getContrasena();
    String personajeElegido = registroRequest.getPersonajeElegido();

    // Verifica si el usuario ya existe
    if (jugadorRepository.buscarPorNombreUsuario(nombreUsuario) != null) {
        return false; // Usuario ya existe
    }
    String hash = hashContrasena(contrasena);
    Jugador nuevoJugador;
    switch (personajeElegido.toLowerCase()) {
        case "granjero":
            nuevoJugador = new Granjero(nombreUsuario, 100, 100, nombreUsuario, hash);
            break;
        case "minero":
            nuevoJugador = new Minero(nombreUsuario, 100, 100, nombreUsuario, hash);
            break;
        case "ligon":
            nuevoJugador = new Ligon(nombreUsuario, 100, 100, nombreUsuario, hash);
            break;
        default:
            return false;
    }
    return jugadorRepository.guardarJugador(nuevoJugador);
}

    /**
     * Intenta iniciar sesión un usuario.
     * @param nombreUsuario Nombre de usuario.
     * @param contrasena Contraseña sin hashear.
     * @return El objeto Jugador si el login es exitoso, null en caso contrario.
     */
    public Jugador login(String nombreUsuario, String contrasena) {
        Jugador jugador = jugadorRepository.buscarPorNombreUsuario(nombreUsuario);

        if (jugador == null) {
            System.out.println("Login fallido: Usuario no encontrado.");
            return null;
        }

        // Hashear la contraseña proporcionada para compararla con el hash almacenado
        String contrasenaHasheada = hashContrasena(contrasena);

        if (contrasenaHasheada != null && contrasenaHasheada.equals(jugador.getContrasenaHash())) {
            System.out.println("Login exitoso. ¡Bienvenido, " + jugador.getNombreUsuario() + "!");
            return jugador;
        } else {
            System.out.println("Login fallido: Contraseña incorrecta.");
            return null;
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param nombreUsuario Nombre de usuario.
     * @param contrasena Contraseña.
     * @param personajeElegido Tipo de personaje ("Granjero", "Minero", "Ligon").
     * @return true si el registro es exitoso, false en caso contrario.
     */
    public boolean registrarUsuario(String nombreUsuario, String contrasena, String personajeElegido) {
        // Verificar si el nombre de usuario ya existe
        if (jugadorRepository.buscarPorNombreUsuario(nombreUsuario) != null) {
            System.out.println("Error de registro: El nombre de usuario '" + nombreUsuario + "' ya está en uso.");
            return false;
        }

        // Hashear la contraseña
        String contrasenaHash = hashContrasena(contrasena);
        if (contrasenaHash == null) {
            System.out.println("Error al hashear la contraseña.");
            return false;
        }

        Jugador nuevoJugador;
        // Instanciar el tipo de jugador correcto
        switch (personajeElegido.toLowerCase()) {
            case "granjero":
                nuevoJugador = new Granjero("Granjero", 100, 50, nombreUsuario, contrasenaHash); // Vida inicial, puntos iniciales
                break;
            case "minero":
                nuevoJugador = new Minero("Minero", 100, 50, nombreUsuario, contrasenaHash);
                break;
            case "ligon":
                nuevoJugador = new Ligon("Ligon", 50, 50, nombreUsuario, contrasenaHash); // Ligón tiene 50 de reputación inicial
                break;
            default:
                System.out.println("Tipo de personaje inválido. Registro fallido.");
                return false;
        }

        // Guardar el nuevo jugador en la base de datos
        if (jugadorRepository.guardarJugador(nuevoJugador)) {
            System.out.println("Usuario '" + nombreUsuario + "' registrado exitosamente como " + personajeElegido + ".");
            return true;
        } else {
            System.out.println("Error al registrar el usuario en la base de datos.");
            return false;
        }
    }

    /**
     * Hashea una contraseña usando SHA-256.
     * NOTA: Para producción, usar un algoritmo más robusto como BCrypt.
     * @param contrasena La contraseña a hashear.
     * @return El hash de la contraseña como String, o null si falla.
     */
    private String hashContrasena(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algoritmo de hashing no encontrado: " + e.getMessage());
            return null;
        }
    }

    // ...existing code...

    /**
    * Busca un jugador por nombre de usuario y devuelve su tipo concreto.
    * @param nombreUsuario Nombre de usuario.
    * @return Instancia concreta de Jugador (Granjero, Minero o Ligon), o null si no existe.
    */
    public Jugador buscarJugadorPorNombreUsuarioConcreto(String nombreUsuario) {
    return jugadorRepository.buscarJugadorPorNombreUsuarioConcreto(nombreUsuario);
    }

// ...existing code...

    /**
     * Obtiene una lista de los mejores jugadores.
     * @param limit Cantidad de jugadores a recuperar.
     * @return Lista de Jugador.
     */
    public List<Jugador> getTopPlayers(int limit) {
        return jugadorRepository.getTopPlayers(limit);
    }

    /**
     * Actualiza la información de un jugador (ej. vida, puntos) en la base de datos.
     * @param jugador El objeto Jugador con los datos actualizados.
     * @return true si se actualiza, false en caso contrario.
     */
    public boolean actualizarJugador(Jugador jugador) {
        return jugadorRepository.actualizarJugador(jugador);
    }
}