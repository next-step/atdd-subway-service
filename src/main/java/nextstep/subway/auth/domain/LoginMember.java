package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeGroup;

public class LoginMember {
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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public double getDiscountPercent() {
        return getAgeGroup().getDiscountPercent();
    }

    private AgeGroup getAgeGroup() {
        return AgeGroup.from(age);
    }
}
