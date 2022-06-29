package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeFarePolicy;

public class LoginMember implements AuthMember {
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

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean isTeenager() {
        return age >= AgeFarePolicy.TEENAGER_MIN_AGE.value() && age <= AgeFarePolicy.TEENAGER_MAX_AGE.value();
    }

    @Override
    public boolean isKid() {
        return age >= AgeFarePolicy.KID_MIN_AGE.value() && age <= AgeFarePolicy.KID_MAX_AGE.value();
    }
}
