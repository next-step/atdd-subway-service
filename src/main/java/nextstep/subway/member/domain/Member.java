package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    protected Member() {
    }

    private Member(final Long id, final String email, final String password, final Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    private Member(final String email, final String password, final Integer age) {
        this(null, email, password, age);
    }

    public static Member of(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }

    public static Member of(final Long id, final String email, final String password, final Integer age) {
        return new Member(id, email, password, age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public void update(final Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(final String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
}
