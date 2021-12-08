package nextstep.subway.auth.dto;

import nextstep.subway.member.dto.MemberRequest;

public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequest of(String email, String password) {
        return new TokenRequest(email, password);
    }

    public static TokenRequest of(MemberRequest request) {
        return new TokenRequest(request.getEmail(), request.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
