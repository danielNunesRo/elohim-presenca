package colegioelohim.com.pontos.services;

import colegioelohim.com.local.entities.LocalEntity;
import colegioelohim.com.local.service.LocalService;
import colegioelohim.com.pontos.dtos.BaterPontoRequestDTO;
import colegioelohim.com.pontos.dtos.PontoResponseDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import colegioelohim.com.pontos.repository.PontosRepository;
import colegioelohim.com.users.entities.UserEntity;
import colegioelohim.com.utils.GeoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PontoService {

    @Inject
    LocalService localService;

    @Inject
    JsonWebToken jwt;

    @Inject
    PontosRepository repository;

    public List<PontoResponseDTO> listarPontos(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(p -> new  PontoResponseDTO(p.dataHora,
                        p.valido,
                        p.motivoInvalidacao))
                .toList();
    }

    public List<PontoResponseDTO> listarPontosDoUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(p -> new PontoResponseDTO(
                        p.dataHora,
                        p.valido,
                        p.motivoInvalidacao
                ))
                .toList();
    }

    @Transactional
    public PontosEntity baterPonto(BaterPontoRequestDTO dto) {

        UserEntity usuario = UserEntity.findById(
                UUID.fromString(jwt.getSubject())
        );

        LocalEntity local = localService.getLocalPadrao();

        double distancia = GeoUtils.calcularDistanciaMetros(
                local.latitude,
                local.longitude,
                dto.latitude,
                dto.longitude
        );

        boolean dentroDaArea = distancia <= local.raioPermitidoMetros;
        LocalDateTime horaSubtraida = LocalDateTime.now().minusHours(3);


        PontosEntity ponto = new PontosEntity();
        ponto.usuario = usuario;
        ponto.local = local;
        ponto.dataHora = horaSubtraida;
        ponto.latitude = dto.latitude;
        ponto.longitude = dto.longitude;
        ponto.valido = dentroDaArea;

        if (!dentroDaArea) {
            ponto.motivoInvalidacao = "Fora da área permitida";
        }

        ponto.persist();
        return ponto;

    }



}
