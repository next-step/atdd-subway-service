package nextstep.subway.auth.domain;

import java.util.Objects;
import nextstep.subway.member.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Age age) {
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

    public Age getAge() {
        if (Objects.isNull(age)) {
            return Age.valueOf(Age.MIN_NUM);
        }
        return age;
    }
}
