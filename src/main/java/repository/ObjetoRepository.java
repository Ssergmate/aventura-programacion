package repository;

import model.Objeto;
import java.util.List;

public interface ObjetoRepository {
    Objeto findById(int idObjeto);
    List<Objeto> findAll();
    // Podrías tener métodos para filtrar objetos por tipo, etc.
}