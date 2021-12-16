package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean isGuest;

    public LoginMember() {
        this.isGuest = true;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.isGuest = false;
    }

    public static LoginMember login(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
    }

    public static LoginMember guest() {
        return new LoginMember();
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

    public boolean isGuest() {
        return isGuest;
    }
}
