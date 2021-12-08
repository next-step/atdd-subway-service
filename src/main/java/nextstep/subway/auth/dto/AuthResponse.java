package nextstep.subway.auth.dto;

public class AuthResponse {

    private String accessToken;

    public static AuthResponse of(String accessToken){
        return new AuthResponse(accessToken);
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
