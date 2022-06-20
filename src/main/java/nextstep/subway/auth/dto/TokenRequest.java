package nextstep.subway.auth.dto;

public class TokenRequest {
    private String email;
    private String password;

    protected TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static TokenRequest of(String email, String password) {
        return new TokenRequest(email, password);
    }
}
