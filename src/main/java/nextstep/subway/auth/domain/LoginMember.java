package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.fare.discount.AgeDiscountPolicy;
import nextstep.subway.path.domain.fare.discount.DiscountPolicy;

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

    public boolean isLoggedIn() {
        return this.id != null;
    }

    public DiscountPolicy createAgeDiscount() {
        return AgeDiscountPolicy.from(age);
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
