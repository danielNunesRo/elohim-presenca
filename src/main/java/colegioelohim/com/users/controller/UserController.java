package colegioelohim.com.users.controller;

import colegioelohim.com.pontos.dtos.PontoResponseDTO;
import colegioelohim.com.users.entities.UserEntity;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/users")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @GET
    @RolesAllowed({"ADMIN"})
    public Response allUsers() {
        List<UserEntity> allUsers = UserEntity.findAll().list();
        return Response.ok(allUsers).build();

    }
}
