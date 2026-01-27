package colegioelohim.com.pontos.controller;


import colegioelohim.com.pontos.dtos.BaterPontoRequestDTO;
import colegioelohim.com.pontos.dtos.BaterPontoResponseDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import colegioelohim.com.pontos.services.PontoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/pontos")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PontosController {

    @Inject
    PontoService pontoService;

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    public Response baterPonto(@Valid BaterPontoRequestDTO dto) {
        PontosEntity ponto = pontoService.baterPonto(dto);

        return Response.ok(new BaterPontoResponseDTO(ponto)).build();
    }

}
