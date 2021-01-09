package nextstep.subway.auth.domain;

import com.sun.istack.NotNull;

public class LoginMember {
    @NotNull
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    //만 6세 이상 만 13세 미만
    public boolean isChild() {
        return this.age >= 6 && this.age < 13;
    }

    //만 13세 이상 - 만 18세 이하
    public boolean isTeenager() {
        return this.age >= 13 && this.age <= 18;
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
