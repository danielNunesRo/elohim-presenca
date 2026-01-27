package colegioelohim.com.pontos.dtos;

import jakarta.validation.constraints.NotNull;

public class BaterPontoRequestDTO {

    @NotNull
    public double latitude;

    @NotNull
    public double longitude;

}
