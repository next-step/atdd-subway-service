package nextstep.subway.auth.acceptance;

public class AuthToken {
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken() {
    }

    public void changeToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
