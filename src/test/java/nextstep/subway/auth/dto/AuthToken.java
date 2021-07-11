package nextstep.subway.auth.dto;

public class AuthToken {
    private String accessToken;

    public AuthToken() {

    }

    public AuthToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void changeToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
