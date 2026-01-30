package colegioelohim.com.pontos.dtos;

import java.time.LocalDateTime;

public class PontoUsuarioDTO {

    public String nome;
    public String email;
    public LocalDateTime dataHora;
    public boolean valido;
    public String motivoInvalidacao;

    public PontoUsuarioDTO(
            String nome,
            String email,
            LocalDateTime dataHora,
            boolean valido,
            String motivoInvalidacao
    ) {
        this.nome = nome;
        this.email = email;
        this.dataHora = dataHora;
        this.valido = valido;
        this.motivoInvalidacao = motivoInvalidacao;
    }
}

