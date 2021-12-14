package nextstep.subway.auth.dto;

public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest() {
    }

    private TokenRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequest of(final String email, final String password) {
        return new TokenRequest(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
