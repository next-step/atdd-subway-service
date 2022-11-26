package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Email;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, Email email, Age age) {
        this.id = id;
        this.email = email.value();
        this.age = age.value();
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
