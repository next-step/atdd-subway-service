package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeDiscountType;
import nextstep.subway.path.domain.policy.DiscountPolicy;
import nextstep.subway.path.domain.policy.NonDiscountPolicy;

public class LoginMember implements LoginAbleMember {
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

    @Override
    public DiscountPolicy getDiscountPolicy() {
        DiscountPolicy discountPolicyByAge = AgeDiscountType.findDiscountPolicyByAge(this.age);
        return discountPolicyByAge != null ? discountPolicyByAge : new NonDiscountPolicy();
    }
}
