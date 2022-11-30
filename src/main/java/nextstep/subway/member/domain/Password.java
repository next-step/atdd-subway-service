package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.exception.MemberExceptionCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class Password {

    private static final Pattern REGEX = Pattern.compile("([a-zA-Z0-9!@#$%^&*]{10,16})");

    @Column(nullable = false)
    private String password;

    protected Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if(StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException(MemberExceptionCode.REQUIRED_PASSWORD.getMessage());
        }

        if(!Pattern.matches(REGEX.pattern(), password)) {
            throw new IllegalArgumentException(MemberExceptionCode.NOT_PASSWORD_FORMAT.getMessage());
        }
    }

    public void checkPassword(String password) {
        if(!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException(MemberExceptionCode.PASSWORD_NOT_MATCH.getMessage());
        }
    }
}
