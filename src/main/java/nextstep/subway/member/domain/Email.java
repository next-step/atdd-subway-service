package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.exception.MemberExceptionType;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private final String value;

    private Email(final String email) {
        validation(email);
        this.value = email;
    }

    public static Email of(final String email) {
        return new Email(email);
    }

    private void validation(final String email) {
        if (notRegexEmail(email)) {
            throw new MemberException(MemberExceptionType.NOT_EMAIL_REGEX);
        }
    }

    private boolean notRegexEmail(final String email) {
        return !EMAIL_PATTERN.matcher(email).matches();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
