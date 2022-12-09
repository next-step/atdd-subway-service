package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;
    private DiscountPolicy discountPolicy;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Age age, DiscountPolicy discountPolicy) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.discountPolicy = discountPolicy;
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

    public Money getCharge() {
        return discountPolicy.getCharge();
    }
}
