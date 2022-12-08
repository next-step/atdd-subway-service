package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;
    private DiscountPolicy discountPolicy;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.from(age);
    }

    public LoginMember(Long id, String email, Age age) {
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

    public Age getAge() {
        return age;
    }

    public void addDiscountPolicy(DiscountPolicy discountPolicyByAge) {
        this.discountPolicy = discountPolicyByAge;
    }
}
