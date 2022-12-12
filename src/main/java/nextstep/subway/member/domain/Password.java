package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.enums.ErrorMessage;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {
    @Column(nullable = false)
    private String password;

    protected Password() {}

    private Password(String password) {
        validateEmpty(password);
        this.password = password;
    }

    private void validateEmpty(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_BLANK.getMessage());
        }
    }

    public static Password from(String password) {
        return new Password(password);
    }

    public void equalsPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException(ErrorMessage.UNAUTHORIZED.getMessage());
        }
    }

    public String value() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
