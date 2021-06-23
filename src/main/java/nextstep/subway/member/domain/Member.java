package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.AuthorizationException;
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
            throw new AuthorizationException();
        }
    }

    public boolean isFree() {
        return age <= 5 || age >= 65;
    }

    public boolean isAdult() {
        return id == null || (age <= 64 && age >= 19);
    }

    public boolean isYouth() {
        return age >= 13 && age <= 18;
    }

    public boolean isChild() {
        return age >= 6 && age <= 12;
    }

}
