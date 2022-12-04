package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.domain.LoginMember;

import java.util.Objects;

public class LoginMemberThreadLocal {
    private static final ThreadLocal<LoginMember> LOGIN_MEMBER = new ThreadLocal<>();

    private LoginMemberThreadLocal() {
    }

    public static void set(LoginMember loginMember) {
        LOGIN_MEMBER.set(loginMember);
    }

    public static LoginMember get() {
        LoginMember loginMember = LOGIN_MEMBER.get();
        if (Objects.isNull(loginMember)) {
            return LoginMember.ANONYMOUS;
        }
        return  loginMember;
    }

    public void unLoad() {
        LOGIN_MEMBER.remove();
    }
}
