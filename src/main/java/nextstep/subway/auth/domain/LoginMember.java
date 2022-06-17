package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private DiscountPolicy discountPolicy;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age, DiscountPolicy discountPolicy) {
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

    public Integer getAge() {
        return age;
    }
}
