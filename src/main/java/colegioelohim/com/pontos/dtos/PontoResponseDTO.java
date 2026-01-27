package colegioelohim.com.pontos.dtos;

import java.time.LocalDateTime;

public class PontoResponseDTO {

    public LocalDateTime dataHora;
    public boolean valido;
    public String motivo;

    public PontoResponseDTO(LocalDateTime dataHora, boolean valido, String motivo) {
        this.dataHora = dataHora;
        this.valido = valido;
        this.motivo = motivo;
    }

}
