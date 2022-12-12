package nextstep.subway.auth.domain;

public class LoginMember {
    private static final int ADULT = 19;

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {}

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember noneLoginMember() {
        return new LoginMember(null, null, ADULT);
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
