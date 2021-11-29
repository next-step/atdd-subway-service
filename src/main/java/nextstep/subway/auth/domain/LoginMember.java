package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    private LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
    }

    public static LoginMember createEmpty() {
        return new LoginMember();
    }

    public boolean isNoneLoginMember() {
        return Objects.isNull(id);
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
