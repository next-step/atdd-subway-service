package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private MemberType memberType;

    private LoginMember() {
        memberType = MemberType.GUEST;
    }

    public static LoginMember guest() {
        return new LoginMember();
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.memberType = MemberType.MEMBER;
    }

    public Long getId() {
        if (isGuest()) {
            throw new AuthorizationException();
        }
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return new Member();
    }

    public boolean isGuest() {
        return memberType.isGuest();
    }
}
