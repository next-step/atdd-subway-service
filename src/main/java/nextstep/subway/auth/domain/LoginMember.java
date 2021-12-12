package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean isLogin;

    private LoginMember() {

    }

    public static LoginMember notLoggedIn() {
        LoginMember loginMember = new LoginMember();
        loginMember.isLogin = false;
        return loginMember;
    }

    public static LoginMember loggedIn(Long id, String email, Integer age) {
        LoginMember loginMember = new LoginMember();
        loginMember.id = id;
        loginMember.email = email;
        loginMember.age = age;
        loginMember.isLogin = true;
        return loginMember;
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

    public boolean isLogin() {
        return isLogin;
    }
}
