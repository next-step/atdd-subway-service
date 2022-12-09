package nextstep.subway.auth.domain;

public class LoginMember {

    public static final LoginMember GUEST = new LoginMember();

    private Long id;
    private String email;
    private Integer age;
    private UserType userType;

    private LoginMember() {
        this.userType = UserType.GUEST;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.userType = UserType.MEMBER;
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

    public UserType getUserType() {
        return userType;
    }
}
