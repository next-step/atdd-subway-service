package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Member;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    private MemberType memberType;

    private LoginMember() {
    }

    private LoginMember(Long id, String email, Integer age, MemberType memberType) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.memberType = memberType;
    }

    public static LoginMember ofByLogin(Long id, String email, Integer age) {
        return new LoginMember(id, email, age, MemberType.LOGIN);
    }

    public static Object ofByNotLogin() {
        return new LoginMember(null, null, null, MemberType.NOT_LOGIN);
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
