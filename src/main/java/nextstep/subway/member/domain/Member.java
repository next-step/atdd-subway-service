package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.exception.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    public static final String ERROR_MESSAGE_VALID_ID_OR_PASSWORD = "아이디 또는 비밀번호가 일치하지 않습니다.";

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

    public void validPassword(String password) {
        if (checkPassword(password)) {
            throw new AuthorizationException(ERROR_MESSAGE_VALID_ID_OR_PASSWORD);
        }
    }

    private boolean checkPassword(String password) {
        return !StringUtils.equals(this.password, password);
    }
}
