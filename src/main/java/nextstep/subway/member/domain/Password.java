package nextstep.subway.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.utils.StringUtils;

@Embeddable
public class Password {

    @Column(nullable = false)
    private String password;

    protected Password() {}

    private Password(String password) {
        validatePasswordNullOrEmpty(password);
        this.password = password;
    }

    public static Password from(String password) {
        return new Password(password);
    }

    private void validatePasswordNullOrEmpty(String password) {
        if(StringUtils.isNullOrEmpty(password)) {
            throw new IllegalArgumentException(ErrorCode.비밀번호는_비어있을_수_없음.getErrorMessage());
        }
    }

    public void checkPassword(String password) {
        if (!Objects.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
