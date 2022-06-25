package nextstep.subway.auth.dto;

import java.util.Objects;

public class TokenRequest {
    private final String email;
    private final String password;

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

    @Override
    public String toString() {
        return "TokenRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TokenRequest that = (TokenRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
