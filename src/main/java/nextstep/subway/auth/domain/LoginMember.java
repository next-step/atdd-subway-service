package nextstep.subway.auth.domain;

import nextstep.subway.fare.domain.AgeDiscountFarePolicy;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    protected LoginMember() {

    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember anonymous() {
        return new LoginMember(null, null, AgeDiscountFarePolicy.NORMAL.endAge());
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
