package nextstep.subway.auth.domain;


import nextstep.subway.fare.domain.DiscountAgeType;
import nextstep.subway.policy.DiscountPolicy;

public class LoginMember implements ServiceMember{
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public DiscountPolicy getDiscountPolicy() {
        DiscountAgeType rule = DiscountAgeType.findDiscountAgeRuleType(this.age);
        return rule.getDiscountPolicy();
    }
}
