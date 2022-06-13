package nextstep.subway.auth.domain;

public class LoginMember {
    private static final int DEFAULT_AGE = 19;

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
        this.age = DEFAULT_AGE;
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
