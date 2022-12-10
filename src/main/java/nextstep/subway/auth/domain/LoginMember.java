package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {

    public static final long GUEST_DEFAULT_ID = 0;
    public static final String GUEST_DEFAULT_EMAIL = "guest@guest.com";
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

    public static LoginMember guest() {
        return new LoginMember(GUEST_DEFAULT_ID, GUEST_DEFAULT_EMAIL, Age.defaultAge(), DiscountPolicy.defaultPolicy());
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
