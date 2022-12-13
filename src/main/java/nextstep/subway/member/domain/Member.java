package nextstep.subway.member.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.exception.AuthorizationException;

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

    public Member() {
    }

    public Member(Email email, Password password, Age age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public Email email() {
        return email;
    }

    public Password password() {
        return password;
    }

    public Age age() {
        return age;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(Password password) {
        if (this.password.notEquals(password)) {
            throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
        }
    }
}
