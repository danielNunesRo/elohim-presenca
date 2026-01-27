package colegioelohim.com.pontos.repository;

import colegioelohim.com.pontos.entities.PontosEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PontosRepository implements PanacheRepository<PontosEntity> {

    public List<PontosEntity> findByUsuarioId(UUID usuarioId) {
        return find("usuario.id = ?1 and valido = true order by dataHora desc", usuarioId).list();
    }
}
