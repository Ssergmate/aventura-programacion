package repository;

import model.Enemigo;
import java.util.List;

public interface EnemigoRepository {
    Enemigo findById(int idEnemigo);
    Enemigo findByName(String nombre); // Para cargar enemigos por nombre
    List<Enemigo> findAll();
    // Podrías tener métodos para cargar enemigos con sus ataques, etc.
}