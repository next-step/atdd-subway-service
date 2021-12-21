package nextstep.subway.auth.domain;

public class LoginMember {

    public static final LoginMember GUEST = new LoginMember();

    private Long id;
    private String email;
    private Age age;

    protected LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.of(age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }
}
