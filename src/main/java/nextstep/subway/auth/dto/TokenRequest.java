package nextstep.subway.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "TokenRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

