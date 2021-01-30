package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.dto.Money;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {

    private static final int CHILD_START_AGE = 6;
    private static final int CHILD_END_AGE = 13;
    private static final int TEENAGE_END_AGE = 19;

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

    public DiscountStrategy getDiscountStrategy() {
        if(isChild()){
            return new ChildDiscountStrategy();
        }
        if(isTeenager()){
            return new TeenagerDiscountStrategy();
        }
        return new NoDiscountStrategy();
    }

    private boolean isChild() {
        return this.age >= CHILD_START_AGE && this.age < CHILD_END_AGE;
    }

    private boolean isTeenager() {
        return this.age >= CHILD_END_AGE && this.age < TEENAGE_END_AGE;
    }
}
