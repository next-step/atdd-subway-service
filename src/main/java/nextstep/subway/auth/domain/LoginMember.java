package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Member;

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

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean isLoginMember() {
        return true;
    }

    @Override
    public boolean isStranger() {
        return false;
    }

    public String getEmail() {
        return email;
    }
}
