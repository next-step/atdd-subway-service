package nextstep.subway.member.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MemberEmail {
    private String email;

    protected MemberEmail() {
    }

    public MemberEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEmail that = (MemberEmail) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
