package nextstep.subway.auth.dto;

import nextstep.subway.auth.domain.PasswordSupplier;

public class TokenRequest implements PasswordSupplier {
    private String email;
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
}
