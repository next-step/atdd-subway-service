package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Objects;

import static nextstep.subway.utils.Message.WRONG_PASSWORD;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException(WRONG_PASSWORD);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(age, member.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age);
    }
}
