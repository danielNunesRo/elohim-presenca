package colegioelohim.com.pontos.repository;

import colegioelohim.com.pontos.dtos.PontoUsuarioDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PontosRepository implements PanacheRepository<PontosEntity> {

    public List<PontoUsuarioDTO> listarPontosComUsuario(UUID usuarioId) {
        return getEntityManager().createQuery("""
                SELECT new colegioelohim.com.pontos.dtos.PontoUsuarioDTO(
                    u.nome,
                    u.email,
                    p.dataHora,
                    p.valido,
                    p.motivoInvalidacao)
                FROM PontosEntity p
                JOIN p.usuario u
                WHERE u.id = :usuarioId
                ORDER BY p.dataHora ASC
                """, PontoUsuarioDTO.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    public List<PontosEntity> findByUsuarioId(UUID usuarioId) {
        return find("usuario.id = ?1 and valido = true order by dataHora desc", usuarioId).list();
    }
}
