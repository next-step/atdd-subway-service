package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.path.domain.Fare;

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

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
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

    public int pays(Fare fare) {
        if (isAdult()) {
            return fare.adultFare();
        }

        if (isTeenager()) {
            return fare.teenagerFare();
        }

        if (isChild()) {
            return fare.childFare();
        }

        return Fare.FREE;
    }

    private boolean isAdult() {
        return age >= 20;
    }

    private boolean isTeenager() {
        return 13 <= age && age < 19;
    }

    private boolean isChild() {
        return 6 <= age && age < 13;
    }
}
