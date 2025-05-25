package repository;

import model.Juego;
import java.util.List;

public interface JuegoRepository {
    Juego findById(int idJuego);
    List<Juego> findByTipoJuego(String tipo); // Para filtrar minijuegos por tipo (ej: "Adivinanza", "DecisionRapida")
    List<Juego> findAll();
}