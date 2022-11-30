package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;
import org.springframework.util.StringUtils;

import javax.persistence.Column;

public class Password {
    private static final String ERROR_MESSAGE_NOT_BLANK_PASSWORD = "비밀번호를 입력해주세요.";

    @Column(nullable = false)
    private String password;

    protected Password() {}

    private Password(String password) {
        validPassword(password);
        this.password = password;
    }

    public static Password from(String password) {
        return new Password(password);
    }

    private void validPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new InvalidParameterException(ERROR_MESSAGE_NOT_BLANK_PASSWORD);
        }
    }

    public boolean checkPassword(String password) {
        return !this.password.equals(password);
    }

    public String value() {
        return password;
    }
}
