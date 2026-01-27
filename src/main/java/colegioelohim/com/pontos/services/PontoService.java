package colegioelohim.com.pontos.services;

import colegioelohim.com.local.entities.LocalEntity;
import colegioelohim.com.local.service.LocalService;
import colegioelohim.com.pontos.dtos.BaterPontoRequestDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import colegioelohim.com.users.entities.UserEntity;
import colegioelohim.com.utils.GeoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class PontoService {

    @Inject
    LocalService localService;

    @Inject
    JsonWebToken jwt;

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

        PontosEntity ponto = new PontosEntity();
        ponto.usuario = usuario;
        ponto.local = local;
        ponto.dataHora = LocalDateTime.now();
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
