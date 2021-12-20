package nextstep.subway.auth.dto;

public class TokenResponse {
    private String accessToken;

    protected TokenResponse() {
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
