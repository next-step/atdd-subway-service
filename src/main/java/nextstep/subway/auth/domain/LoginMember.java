package nextstep.subway.auth.domain;

import nextstep.subway.fare.domain.AgePolicy;

public class LoginMember {
    private Long id;
    private String email;
    private int age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
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

    public AgePolicy getAgePolicy() {
        return AgePolicy.of(this.age);
    }
}
