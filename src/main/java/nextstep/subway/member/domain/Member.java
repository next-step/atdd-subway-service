package nextstep.subway.member.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.exception.AuthorizationException;
import org.springframework.util.Assert;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    @Embedded
    private Age age;

    protected Member() {
    }

    private Member(Email email, Password password, Age age) {
        Assert.notNull(email, "이메일은 필수입니다.");
        Assert.notNull(password, "패스워드는 필수입니다.");
        Assert.notNull(age, "나이는 필수입니다.");
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Member of(Email email, Password password, Age age) {
        return new Member(email, password, age);
    }

    public Long id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Age age() {
        return age;
    }

    public void update(Member member) {
        Assert.notNull(member, "수정할 회원 정보는 필수입니다.");
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(Password password) {
        if (this.password.notEquals(password)) {
            throw new AuthorizationException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(email, member.email);
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", email=" + email +
            ", password=" + password +
            ", age=" + age +
            '}';
    }
}
