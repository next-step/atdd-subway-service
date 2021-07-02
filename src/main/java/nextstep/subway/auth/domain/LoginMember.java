package nextstep.subway.auth.domain;

public class LoginMember {
    private static final long DEFAULT_ID = -1L;

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public static LoginMember createDefaultLoginMember() {
        return new LoginMember(DEFAULT_ID, "", 0);
    }
}
