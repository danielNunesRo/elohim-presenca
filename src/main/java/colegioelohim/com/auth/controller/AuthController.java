package colegioelohim.com.auth.controller;

import colegioelohim.com.auth.dtos.LoginRequestDTO;
import colegioelohim.com.auth.dtos.TokenResponseDTO;
import colegioelohim.com.auth.services.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDTO dto) {
        String token = authService.authenticate(dto);
        return Response.ok(new TokenResponseDTO(token, 3600)).build();
    }



}
