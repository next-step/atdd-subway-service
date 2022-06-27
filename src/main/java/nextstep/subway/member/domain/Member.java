package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.exception.MemberExceptionType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    @Column(nullable = false)
    private String password;
    @Embedded
    private Age age;

    protected Member() {}

    private Member(final String email, final String password, final Integer age) {
        this.email = Email.of(email);
        this.password = password;
        this.age = Age.of(age);
    }

    public static Member of(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public Integer getAge() {
        return age.getValue();
    }

    public void update(final Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(final String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new MemberException(MemberExceptionType.INVALID_PASSWORD);
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email=" + email +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(age, member.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age);
    }
}
