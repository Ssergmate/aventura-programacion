package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // --- Configuración de la base de datos ---
    private static final String URL = "jdbc:mysql://localhost:3306/stardew_valley_db"; // Reemplaza 'stardew_valley_db' con el nombre de tu BD
    private static final String USER = "your_username"; // Reemplaza con tu usuario de MySQL
    private static final String PASSWORD = "your_password"; // Reemplaza con tu contraseña de MySQL

    /**
     * Establece una conexión con la base de datos.
     * @return Objeto Connection si la conexión es exitosa, null en caso contrario.
     */
    public static Connection getConnection() {
        try {
            // Cargar el driver JDBC (no siempre es necesario en versiones recientes de JDBC, pero es buena práctica)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de MySQL no encontrado. Asegúrate de que mysql-connector-java esté en tu classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cierra una conexión a la base de datos de forma segura.
     * @param connection La conexión a cerrar.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para crear las tablas si no existen.
     * Deberías llamar a esto una vez al inicio de tu aplicación (ej. en el main).
     */
    public static void createTables() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            if (conn != null) {
                stmt = conn.createStatement();

                // Tabla Jugadores
                String createJugadoresTable = "CREATE TABLE IF NOT EXISTS Jugadores (" +
                                              "id INT AUTO_INCREMENT PRIMARY KEY," +
                                              "nombre_usuario VARCHAR(50) NOT NULL UNIQUE," +
                                              "contrasena_hash VARCHAR(255) NOT NULL," +
                                              "tipo_personaje VARCHAR(20) NOT NULL," +
                                              "vida INT DEFAULT 100," +
                                              "puntos INT DEFAULT 50," +
                                              "puntuacion_total INT DEFAULT 0" + // Se calcula, pero se puede guardar
                                              ");";
                stmt.execute(createJugadoresTable);
                System.out.println("Tabla 'Jugadores' verificada/creada.");

                // Si necesitas más tablas, por ejemplo, para objetos, enemigos, etc.
                // String createObjetosTable = "CREATE TABLE IF NOT EXISTS Objetos (...)";
                // stmt.execute(createObjetosTable);

            }
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas de la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}