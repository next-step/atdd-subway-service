package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.exception.FavoriteExceptionCode;
import nextstep.subway.member.exception.MemberExceptionCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    private static final Pattern REGEX = Pattern.compile("(^[a-zA-Z0-9_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})");

    @Column(unique = true, nullable = false)
    private String email;

    protected Email() {
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if(StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException(MemberExceptionCode.REQUIRED_EMAIL.getMessage());
        }

        if(!Pattern.matches(REGEX.pattern(), email)) {
            throw new IllegalArgumentException(MemberExceptionCode.NOT_EMAIL_FORMAT.getMessage());
        }
    }

    public void checkEmail(String email) {
        if(!this.email.equals(email)) {
            throw new AuthorizationException(MemberExceptionCode.EMAIL_NOT_MATCH.getMessage());
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
