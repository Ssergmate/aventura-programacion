package repository;

import model.Jugador;
import model.Granjero;
import model.Minero;
import model.Ligon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JugadorRepository {

    /**
     * Guarda un nuevo jugador en la base de datos.
     * @param jugador El objeto Jugador a guardar.
     * @return true si se guarda correctamente, false en caso contrario.
     */
    public boolean guardarJugador(Jugador jugador) {
        String sql = "INSERT INTO Jugadores (nombre_usuario, contrasena_hash, tipo_personaje, vida, puntos, puntuacion_total) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, jugador.getNombreUsuario());
                pstmt.setString(2, jugador.getContrasenaHash());
                pstmt.setString(3, jugador.getNombre()); // "Granjero", "Minero", "Ligon"
                pstmt.setInt(4, jugador.getVida());
                pstmt.setInt(5, jugador.getPuntos());
                pstmt.setInt(6, jugador.getPuntuacionTotal()); // Guardar la puntuación inicial

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Si necesitas el ID generado, puedes asignarlo al objeto jugador
                            // jugador.setId(generatedKeys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar jugador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Busca un jugador por su nombre de usuario.
     * @param nombreUsuario El nombre de usuario a buscar.
     * @return El objeto Jugador encontrado, o null si no existe.
     */
    public Jugador buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id, nombre_usuario, contrasena_hash, tipo_personaje, vida, puntos FROM Jugadores WHERE nombre_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nombreUsuario);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    String tipoPersonaje = rs.getString("tipo_personaje");
                    String user = rs.getString("nombre_usuario");
                    String passHash = rs.getString("contrasena_hash");
                    int vida = rs.getInt("vida");
                    int puntos = rs.getInt("puntos");

                    // Crear la instancia del tipo de jugador correcto
                    switch (tipoPersonaje.toLowerCase()) {
                        case "granjero":
                            return new Granjero(tipoPersonaje, vida, puntos, user, passHash);
                        case "minero":
                            return new Minero(tipoPersonaje, vida, puntos, user, passHash);
                        case "ligon":
                            return new Ligon(tipoPersonaje, vida, puntos, user, passHash);
                        default:
                            System.err.println("Tipo de personaje desconocido cargado de la BD: " + tipoPersonaje);
                            return new Jugador(tipoPersonaje, vida, puntos, user, passHash) {}; // Jugador anónimo
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar jugador por nombre de usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(conn);
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Jugador buscarJugadorPorNombreUsuarioConcreto(String nombreUsuario) {
    // Suponiendo que tienes una columna 'tipo' en la tabla jugadores
    // y los constructores de Granjero, Minero y Ligon aceptan los mismos parámetros
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM jugadores WHERE nombre_usuario = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombreUsuario);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String tipo = rs.getString("tipo");
            String nombre = rs.getString("nombre");
            int vida = rs.getInt("vida");
            int puntos = rs.getInt("puntos");
            String contrasenaHash = rs.getString("contrasena_hash");
            // Puedes agregar más campos según tu modelo

            switch (tipo.toLowerCase()) {
                case "granjero":
                    return new Granjero(nombre, vida, puntos, nombreUsuario, contrasenaHash);
                case "minero":
                    return new Minero(nombre, vida, puntos, nombreUsuario, contrasenaHash);
                case "ligon":
                    return new Ligon(nombre, vida, puntos, nombreUsuario, contrasenaHash);
                default:
                    return null;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }



    /**
     * Actualiza los datos de un jugador existente en la base de datos.
     * @param jugador El objeto Jugador con los datos actualizados.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    public boolean actualizarJugador(Jugador jugador) {
        String sql = "UPDATE Jugadores SET vida = ?, puntos = ?, puntuacion_total = ? WHERE nombre_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, jugador.getVida());
                pstmt.setInt(2, jugador.getPuntos());
                pstmt.setInt(3, jugador.getPuntuacionTotal()); // Asegúrate de actualizar la puntuación total
                pstmt.setString(4, jugador.getNombreUsuario());

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar jugador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Obtiene una lista de los mejores jugadores por puntuación total.
     * @param limit El número máximo de jugadores a devolver.
     * @return Una lista de objetos Jugador ordenados por puntuación total de forma descendente.
     */
    public List<Jugador> getTopPlayers(int limit) {
        List<Jugador> topPlayers = new ArrayList<>();
        String sql = "SELECT nombre_usuario, tipo_personaje, vida, puntos, puntuacion_total FROM Jugadores ORDER BY puntuacion_total DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, limit);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String tipoPersonaje = rs.getString("tipo_personaje");
                    int vida = rs.getInt("vida");
                    int puntos = rs.getInt("puntos");
                    int puntuacionTotal = rs.getInt("puntuacion_total"); // Aunque lo calculamos, lo obtenemos

                    // Crear una instancia de Jugador (puedes crear el tipo específico si quieres)
                    // Para el TOP, un Jugador base es suficiente si solo muestras nombre y puntuación.
                    // Si quieres mostrar atributos específicos del tipo, necesitarás el switch.
                    Jugador jugador = new Jugador(tipoPersonaje, vida, puntos, nombreUsuario, "DUMMY_HASH") {}; // Contraseña hash dummy para este caso
                    jugador.setPuntos(puntos); // Asegurarse de que los puntos estén correctos
                    jugador.setVida(vida); // Asegurarse de que la vida esté correcta
                    // Como la puntuacion_total se guarda en la BD, la asignamos directamente
                    // Si no la guardaras, tendrías que calcularla aquí o en el getter de Jugador.
                    // En este caso, ya la estamos obteniendo.
                    topPlayers.add(jugador);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener top players: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(conn);
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return topPlayers;
    }
}