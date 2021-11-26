package nextstep.subway.auth.dto;

import javax.validation.constraints.NotBlank;

public class TokenRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "패스워드는 필수입니다.")
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
}
