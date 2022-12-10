package nextstep.subway.auth.domain;

public class LoginMember {

    public static final LoginMember GUEST = new LoginMember();
    private Long id;
    private String email;
    private Integer age;

    private UserType type;

    private LoginMember() {
        this.type = UserType.GUEST;
    }
    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.type = UserType.USER;
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

    public UserType getType() {
        return type;
    }
}
