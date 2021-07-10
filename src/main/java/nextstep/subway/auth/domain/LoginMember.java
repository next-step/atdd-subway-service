package nextstep.subway.auth.domain;

import java.util.Objects;

import nextstep.subway.common.domain.UserFarePolicy;
import nextstep.subway.path.domain.DiscountInfo;

public class LoginMember implements DiscountInfo {
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

    public boolean isLoginUser() {
        return !Objects.isNull(this.id) && this.id > 0;
    }

    @Override
    public Double getDiscountRate() {
        if (isLoginUser()) {
            return UserFarePolicy.findDiscountRate(this.age);
        }
        return 1.0;
    }

    @Override
    public int getDiscountFare() {
        if (isLoginUser()) {
            return UserFarePolicy.findDiscountFare(this.age);
        }
        return 0;
    }
}
