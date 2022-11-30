package nextstep.subway.member.domain;

import nextstep.subway.common.exception.InvalidParameterException;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String ERROR_MESSAGE_EMPTY_EMAIL = "이메일을 입력해주세요.";
    private static final String ERROR_MESSAGE_INCORRECT_EMAIL_FORMAT = "이메일 형식이 맞지 않습니다.";

    @Column(nullable = false)
    private String email;

    protected Email() {}

    private Email(String email) {
        validEmail(email);
        this.email = email;
    }

    public static Email from(String email) {
        return new Email(email);
    }

    private void validEmail(String email) {
        validNullAndBlank(email);
        validEmailFormat(email);
    }

    private static void validNullAndBlank(String email) {
        if (!StringUtils.hasText(email)) {
            throw new InvalidParameterException(ERROR_MESSAGE_EMPTY_EMAIL);
        }
    }

    private static void validEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).find()) {
            throw new InvalidParameterException(ERROR_MESSAGE_INCORRECT_EMAIL_FORMAT);
        }
    }

    public String value() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
