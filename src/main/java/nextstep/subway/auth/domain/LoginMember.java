package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.path.domain.AgeType;
import org.springframework.util.ObjectUtils;

public class LoginMember {
    private Long id;
    private String email;
    private int age;


    private LoginMember() {
    }

    public static LoginMember createGuestLoginMember() {
        return new LoginMember();
    }

    public static LoginMember createLoginMember(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
    }

    public void authValidLoginMember() {
        if (isGuest()) {
            throw new AuthorizationException("허가 되지 않은 사용자");
        }
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public boolean isGuest() {
        return ObjectUtils.isEmpty(id);
    }

    public AgeType getAgeType() {
        if (isGuest()) {
            return AgeType.NONE;
        }
        return AgeType.getAgeType(age);
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
}
