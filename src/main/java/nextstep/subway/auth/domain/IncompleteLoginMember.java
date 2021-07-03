package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.exception.AuthorizationException;

import java.util.Objects;

public class IncompleteLoginMember {
    private final LoginMember loginMember;

    public IncompleteLoginMember(LoginMember loginMember) {
        this.loginMember = loginMember;
    }

    public static IncompleteLoginMember ofNull(){
        return new IncompleteLoginMember(null);
    }

    public LoginMember toCompleteLoginMember() {
        if (!isCompleteLoginMember()) {
            throw new AuthorizationException("토큰 검증에 실패하여 멤버 정보를 확인할 수 없습니다.");
        }
        return loginMember;
    }

    public boolean isCompleteLoginMember() {
        return Objects.nonNull(loginMember);
    }
}
