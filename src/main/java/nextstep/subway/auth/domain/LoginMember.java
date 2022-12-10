package nextstep.subway.auth.domain;

import static nextstep.subway.auth.domain.AgeGroup.*;

public class LoginMember {
    public static final LoginMember GUEST = new LoginMember();

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

    public AgeGroup getAgeGroup() {
        if (this.equals(GUEST)) {
            return NO_SALE_AGE;
        }
        return AgeGroup.calculate(this.age);
    }
}
