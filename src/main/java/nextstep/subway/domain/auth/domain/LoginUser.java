package nextstep.subway.domain.auth.domain;

public class LoginUser extends User {

    public LoginUser(final Long id, final String email, final Integer age) {
        super(id, email, age);
    }

    public LoginUser(final Integer age) {
        super(age);
    }

    @Override
    public boolean isChildren() {
        return this.age >= CHILDREN_MINIMUM_AGE && this.age <= CHILDREN_MAXIMUM_AGE;
    }

    @Override
    public boolean isTeenager() {
        return this.age >= TEENAGER_MINIMUM_AGE && this.age <= TEENAGER_MAXIMUM_AGE;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Integer getAge() {
        return this.age;
    }

    @Override
    public boolean isLoginUser() {
        return true;
    }
}
