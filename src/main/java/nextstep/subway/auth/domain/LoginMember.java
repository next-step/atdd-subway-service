package nextstep.subway.auth.domain;

public class LoginMember {
    private final Long id;
    private final String email;
    private final Integer age;

    public LoginMember() {
        this(null, "", 0);
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
}
