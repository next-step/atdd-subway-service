package nextstep.subway.auth.domain;

public class LoginMember {
    private static final LoginMember GUEST = new LoginMember(0);

    private Long id;
    private String email;
    private Integer age;

    private LoginMember(int age) {
        this.age = age;
    }

    public static LoginMember guest() {
        return GUEST;
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
