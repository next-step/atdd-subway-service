package nextstep.subway.member.domain;

import java.util.Objects;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    private static final String INVALID_EMAIL_AND_PASSWORD_ERROR_MESSAGE = "이메일, 비밀번호는 최소 1자 이상이어야 합니다.";
    private static final String INVALID_AGE_ERROR_MESSAGE = "나이는 1살 이상이어야 합니다.";
    private static final int MIN_AGE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {}

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Member of(String email, String password, Integer age) {
        validateMember(email, password, age);
        return new Member(email, password, age);
    }

    private static void validateMember(String email, String password, Integer age) {
        validateEmailAndPassword(email, password);
        validateAge(age);
    }

    private static void validateEmailAndPassword(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException(INVALID_EMAIL_AND_PASSWORD_ERROR_MESSAGE);
        }
    }

    private static void validateAge(Integer age) {
        if (Objects.isNull(age) || age < MIN_AGE) {
            throw new IllegalArgumentException(INVALID_AGE_ERROR_MESSAGE);
        }

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
        validateMember(member.getEmail(), member.getPassword(), member.getAge());

        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
}
