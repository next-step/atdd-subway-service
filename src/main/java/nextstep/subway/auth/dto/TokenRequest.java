package nextstep.subway.auth.dto;

import nextstep.subway.common.domain.Email;
import nextstep.subway.member.domain.Password;

public class TokenRequest {
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

    public String getPassword() {
        return password;
    }

    public Email email() {
        return Email.from(email);
    }

    public Password password() {
        return Password.from(password);
    }
}
