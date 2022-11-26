package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Age age) {
        this.id = id;
        this.email = email;
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
