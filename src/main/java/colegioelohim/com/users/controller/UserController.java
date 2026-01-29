package colegioelohim.com.users.controller;

import colegioelohim.com.pontos.dtos.PontoResponseDTO;
import colegioelohim.com.users.dto.ChangePasswordRequestDTO;
import colegioelohim.com.users.entities.UserEntity;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.postgresql.util.PasswordUtil;

import java.util.List;
import java.util.UUID;

@Path("/users")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response allUsers() {
        List<UserEntity> allUsers = UserEntity.findAll().list();
        return Response.ok(allUsers).build();

    }

    @PUT
    @RolesAllowed({"ADMIN", "USER"})
    @Transactional
    public Response changePassword(ChangePasswordRequestDTO request,
                                   @Context SecurityContext securityContext) {

        String email = jwt.getClaim("email");
        UserEntity usuario = UserEntity.find("email", email).firstResult();

        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        if (!request.currentPassword.equals(usuario.senhaHash)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Senha atual incorreta")
                    .build();
        }

        if (request.newPassword == null || request.newPassword.length() <= 6) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A nova senha deve ter no mínimo 6 caracteres")
                    .build();
        }

        usuario.senhaHash = request.newPassword;

        return Response.ok("Senha alterada com sucesso").build();

    }
}
