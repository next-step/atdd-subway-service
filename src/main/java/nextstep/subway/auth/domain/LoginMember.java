package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.constants.AuthErrorMessages;
import nextstep.subway.member.domain.Member;

public class LoginMember {

    private static final LoginMember emptyLoginMember = new LoginMember();
    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        checkFieldNotMissing(id, email, age);
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember emptyLoginMember() {
        return emptyLoginMember;
    }

    public static LoginMember from(Member member) {
        if (member == null) {
            throw new AuthorizationException(
                    AuthErrorMessages.UNAUTHORIZED_MEMBER_REQUESTED_FAVORITE_CREATION);
        }
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private void checkFieldNotMissing(Long id, String email, Integer age) {
        if (id == null || email == null || age == null) {
            throw new AuthorizationException(AuthErrorMessages.LOGIN_MEMBER_FIELD_MISSING);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
