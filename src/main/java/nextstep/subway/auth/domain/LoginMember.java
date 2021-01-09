package nextstep.subway.auth.domain;

public class LoginMember {
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

    public boolean isChild() {
        return this.age >= 6 && this.age < 13;
    }

    //만 13세 이상 - 만 18세 이하
    public boolean isTeenager() {
        return this.age >= 13 && this.age < 19;
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
