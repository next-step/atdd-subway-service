package nextstep.subway.auth.domain;

public class LoginMember {
    public static final LoginMember EMPTY_LOGIN_MEMBER = new LoginMember();
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

    public static LoginMember emptyMember() {
        return EMPTY_LOGIN_MEMBER;
    }

    public boolean isEmpty() {
        return id == null;
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
