package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.path.domain.DiscountType;

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isLogin() {
        return id != null;
    }

    public void checkLoginUser() {
        if (!isLogin()) {
            throw new AuthorizationException();
        }
    }

    public DiscountType getDiscountType() {
        if (age >= 6 && age < 13) {
            return DiscountType.CHILDREN;
        }
        if (age >= 13 && age < 19) {
            return DiscountType.TEENAGER;
        }
        return DiscountType.NONE;
    }
}
