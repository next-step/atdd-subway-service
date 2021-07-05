package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.fare.discount.AgeDiscount;
import nextstep.subway.path.domain.fare.discount.Discount;

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

    public Discount buildAgeDiscount() {
        return AgeDiscount.from(age);
    }

    public boolean isLoginUser() {
        return this.id != null;
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
