package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.exception.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
public class Member extends BaseEntity {
    public static final String ERROR_MESSAGE_VALID_ID_OR_PASSWORD = "아이디 또는 비밀번호가 일치하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    private String password;
    private Integer age;

    protected Member() {}

    private Member(String email, String password, Integer age) {
        this.email = Email.from(email);
        this.password = password;
        this.age = age;
    }

    public static Member of(String email, String password, Integer age) {
        return new Member(email, password, age);
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

    public Long getId() {
        return id;
    }

    public String emailValue() {
        return email.value();
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }
}
