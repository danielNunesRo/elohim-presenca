package colegioelohim.com.pontos.controller;


import colegioelohim.com.pontos.dtos.BaterPontoRequestDTO;
import colegioelohim.com.pontos.dtos.BaterPontoResponseDTO;
import colegioelohim.com.pontos.dtos.PontoResponseDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import colegioelohim.com.pontos.services.PontoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/pontos")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PontosController {

    @Inject
    JsonWebToken jwt;

    @Inject
    PontoService pontoService;

    @GET
    @Path("/admin/relatorio")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportarExcelUsuario(@QueryParam("usuarioId") UUID usuarioId) {

        if (usuarioId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parâmetro usuarioId é obrigatório")
                    .build();
        }

        byte[] excel = pontoService.gerarExcelUsuario(usuarioId);
        String nomeUsuario = jwt.getClaim("nome");

        return Response.ok(excel)
                .header(
                        "Content-Disposition",
                        "attachment; filename=pontos-" + nomeUsuario + ".xlsx"
                )
                .build();
    }

    @GET
    @Path("/all")
    @RolesAllowed({"ADMIN"})
    public Response listPontosUsuario(@QueryParam("usuarioId") UUID usuarioId) {
        List<PontoResponseDTO> pontosDoUsuario = pontoService.listarPontosDoUsuario(usuarioId);
        return Response.ok(pontosDoUsuario).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public Response listarMeusPontos() {
        UUID usuarioId = UUID.fromString(jwt.getSubject());

        List<PontoResponseDTO> pontos =
                pontoService.listarPontosDoUsuario(usuarioId);

        return Response.ok(pontos).build();
    }


    @POST
    @RolesAllowed({"ADMIN", "USER"})
    public Response baterPonto(@Valid BaterPontoRequestDTO dto) {
        PontosEntity ponto = pontoService.baterPonto(dto);

        return Response.ok(new BaterPontoResponseDTO(ponto)).build();
    }

}
