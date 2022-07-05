package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.MemberType;

public class LoginMember implements User {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean isGuest() {
        return false;
    }
    public MemberType findMemberType() {
        return MemberType.findType(age);
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
