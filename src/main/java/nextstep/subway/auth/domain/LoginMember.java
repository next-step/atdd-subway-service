package nextstep.subway.auth.domain;

public class LoginMember {
    public static final LoginMember NON_MEMBER = new LoginMember();

    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {}

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

    public boolean isNonMember() {
        return id == null
                && email == null
                && age == null;
    }
}
