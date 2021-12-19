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
    private static final int CHILDREN_DOWN_AGE_LIMIT = 6;
    private static final int CHILDREN_UP_AGE_LIMIT = 13;
    private static final int YOUTH_DOWN_AGE_LIMIT = 13;
    private static final int YOUTH_UP_AGE_LIMIT = 19;

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

    public boolean isOfChildAge() {
        return age >= CHILDREN_DOWN_AGE_LIMIT && age < CHILDREN_UP_AGE_LIMIT;
    }

    public boolean isOfYouthAge() {
        return age >= YOUTH_DOWN_AGE_LIMIT && age < YOUTH_UP_AGE_LIMIT;
    }
}
