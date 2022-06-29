package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeType;
import org.springframework.util.ObjectUtils;

public class LoginMember {
    private Long id;
    private String email;
    private int age;


    private LoginMember() {
    }

    public static LoginMember guestLogin() {
        return new LoginMember();
    }

    public static LoginMember memberLogin(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
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
