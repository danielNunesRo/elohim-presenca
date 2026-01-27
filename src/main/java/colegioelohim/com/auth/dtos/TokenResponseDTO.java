package colegioelohim.com.auth.dtos;

public class TokenResponseDTO {

    public String accessToken;

    public String tokenType = "Bearer";

    public long expiresIn;

    public TokenResponseDTO(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

}
