package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {
    }

    public LoginMember(String email, Integer age) {
        this(null, email, age);
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember guest() {
        return new LoginMember(null, null, 0);
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
