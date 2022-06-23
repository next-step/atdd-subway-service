package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = new Age(age);
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

    public AgeGroup getAgeGroup() {
        return AgeGroup.getAgeGroupByAge(this.age);
    }
}
