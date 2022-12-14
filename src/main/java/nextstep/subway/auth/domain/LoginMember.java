package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;

    private MemberType memberType;

    private LoginMember() {
    }

    private LoginMember(Long id, String email, Age age, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.memberType = memberType;
    }

    public static LoginMember ofLogin(Long id, String email, Age age) {
        return new LoginMember(id, email, age, MemberType.LOGIN);
    }

    public static LoginMember ofNotLogin() {
        return new LoginMember(null, null, null, MemberType.NOT_LOGIN);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }


    public MemberType getMemberType() {
        return memberType;
    }
}
