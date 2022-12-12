package nextstep.subway.member.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {
    @Column(nullable = false)
    private String email;

    protected Email() {}

    private Email(String email) {
        validateEmpty(email);
        this.email = email;
    }

    private void validateEmpty(String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_BLANK.getMessage());
        }
    }

    public static Email from(String email) {
        return new Email(email);
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
