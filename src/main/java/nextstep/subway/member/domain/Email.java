package nextstep.subway.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.utils.StringUtils;

@Embeddable
public class Email {

    @Column(nullable = false)
    private String email;

    protected Email() {}

    private Email(String email) {
        validateEmailNullOrEmpty(email);
        this.email = email;
    }

    public static Email from(String email) {
        return new Email(email);
    }

    private void validateEmailNullOrEmpty(String email) {
        if(StringUtils.isNullOrEmpty(email)) {
            throw new IllegalArgumentException(ErrorCode.이메일은_비어있을_수_없음.getErrorMessage());
        }
    }

    public String value() {
        return this.email;
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
