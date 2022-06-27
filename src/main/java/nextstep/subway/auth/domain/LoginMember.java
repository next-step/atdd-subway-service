package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    private static class LoginMemberHolder {
        private static final LoginMember INSTANCE = new LoginMember();
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

    public static LoginMember empty() {
        return LoginMemberHolder.INSTANCE;
    }

    public boolean isEmpty() {
        return id == null && email == null && age == null;
    }
}
