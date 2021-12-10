package nextstep.subway.auth.dto;

public class TokenRequest {
    private final String email;
    private final String password;

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
}
