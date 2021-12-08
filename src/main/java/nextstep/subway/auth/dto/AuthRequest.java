package nextstep.subway.auth.dto;

public class AuthRequest {

    private String email;
    private String password;

    public static AuthRequest of(String name, String password) {
        return new AuthRequest(name, password);
    }

    public AuthRequest(String email, String password) {
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
