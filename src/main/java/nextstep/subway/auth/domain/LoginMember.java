package nextstep.subway.auth.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class LoginMember {
    public static final LoginMember NOT_LOGIN = new LoginMember();

    @NotNull
    private Long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Integer age;

    private LoginMember() {

    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
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

    public boolean isNotLogin() {
        return this == NOT_LOGIN;
    }
}
