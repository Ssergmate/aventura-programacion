package repository;

import model.Escenario;
import java.util.List;

public interface EscenarioRepository {
    Escenario findById(int idEscenario);
    List<Escenario> findAll();
    Escenario findByName(String name);
}