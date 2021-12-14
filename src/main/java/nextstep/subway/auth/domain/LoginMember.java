package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Member;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(int age) {
        this.age = new Age(age);
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = new Age(age);
    }

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    public boolean isChild() {
        return this.age.isChild();
    }

    public boolean isTeenager() {
        return this.age.isTeenager();
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
}
