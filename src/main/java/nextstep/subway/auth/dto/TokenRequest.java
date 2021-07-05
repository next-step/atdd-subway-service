package nextstep.subway.auth.dto;

import nextstep.subway.member.domain.Member;

public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequest of(Member member) {
        return new TokenRequest(member.getEmail(), member.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
