package nextstep.subway.auth.domain;

import java.util.Objects;
import nextstep.subway.generic.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.valueOf(age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }

    public boolean isNotLogin() {
        return Objects.isNull(id);
    }
}
