package nextstep.subway.auth.domain;

import java.util.Objects;
import nextstep.subway.path.domain.ChildDiscountStrategy;
import nextstep.subway.path.domain.DiscountStrategy;
import nextstep.subway.path.domain.NoDiscountStrategy;
import nextstep.subway.path.domain.TeenagerDiscountStrategy;

public class LoginMember {

    public static final LoginMember NO_LOGIN = new LoginMember();

    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {

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

    public DiscountStrategy getDiscountStrategy() {
        if (this.equals(NO_LOGIN)) {
            return new NoDiscountStrategy();
        }

        if (age >= 6 && age < 13) {
            return new ChildDiscountStrategy();
        }

        if (age >= 13 && age < 19) {
            return new TeenagerDiscountStrategy();
        }

        return new NoDiscountStrategy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginMember that = (LoginMember) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email)
            && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }
}
