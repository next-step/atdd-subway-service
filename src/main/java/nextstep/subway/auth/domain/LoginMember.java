package nextstep.subway.auth.domain;

import java.util.Objects;
import nextstep.subway.path.domain.DiscountStrategy;

public class LoginMember implements User {

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
        return DiscountStrategy.of(age);
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
