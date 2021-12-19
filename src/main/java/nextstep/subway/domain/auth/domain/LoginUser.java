package nextstep.subway.domain.auth.domain;

public class LoginUser extends User {

    public LoginUser(final Long id, final String email, final Integer age) {
        super(id, email, age);
    }

    public LoginUser(final Integer age) {
        super(age);
    }
}
