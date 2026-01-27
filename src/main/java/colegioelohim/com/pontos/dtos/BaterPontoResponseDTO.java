package colegioelohim.com.pontos.dtos;

import colegioelohim.com.pontos.entities.PontosEntity;

import java.time.LocalDateTime;

public class BaterPontoResponseDTO {

    public LocalDateTime dataHora;
    public boolean valido;

    public BaterPontoResponseDTO(PontosEntity ponto) {
        this.dataHora = ponto.dataHora;
        this.valido = ponto.valido;
    }

}
