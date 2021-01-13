package nextstep.subway.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginMember {

    private enum Type {
        NORMAL, ANONYMOUS
    }

    private Long id;
    private String email;
    private Integer age;
    private Type loginType = Type.NORMAL;

    public LoginMember(Type loginType) {
        this.loginType = loginType;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public boolean isAnonymous() {
        return loginType == Type.ANONYMOUS;
    }

    public static LoginMember createAnonymousMember() {
        return new LoginMember(Type.ANONYMOUS);
    }
}
