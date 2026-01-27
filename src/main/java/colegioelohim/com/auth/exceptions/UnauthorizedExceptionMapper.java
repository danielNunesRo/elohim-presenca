package colegioelohim.com.auth.exceptions;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.status(401)
                .entity(Map.of(
                        "error", "unauthorized",
                        "message", e.getMessage() != null ? e.getMessage() : "Não autorizado"
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
