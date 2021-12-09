package nextstep.subway.auth.dto;

public class TokenRequest {
    final private String email;
    final private String password;

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
