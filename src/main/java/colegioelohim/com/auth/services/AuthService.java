package colegioelohim.com.auth.services;

import colegioelohim.com.auth.dtos.LoginRequestDTO;
import colegioelohim.com.security.services.JwtService;
import colegioelohim.com.users.entities.UserEntity;
import colegioelohim.com.users.repositories.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    JwtService jwtService;

    public String authenticate(LoginRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.email).orElseThrow(() -> new UnauthorizedException("Email inválidos"));

        if(!user.ativo) {
            throw new UnauthorizedException("Usuário não está cadastrado no sistema. Procurar gestor");
        }

        if(!dto.senha.equals(user.senhaHash)) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        return jwtService.generateToken(user);

    }

}
