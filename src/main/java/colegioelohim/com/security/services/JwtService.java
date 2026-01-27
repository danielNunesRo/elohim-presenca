package colegioelohim.com.security.services;

import colegioelohim.com.users.entities.UserEntity;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtService {

    public String generateToken(UserEntity user) {
        return Jwt.issuer("colegio-elohim").subject(user.id.toString())
                .claim("email", user.email)
                .claim("role", user.role)
                .claim("nome", user.nome)
                .expiresIn(3600)
                .sign();
    }
}
