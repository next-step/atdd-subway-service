package nextstep.subway.auth.domain;

public class LoginMember {

    private static final LoginMember GUEST = new LoginMember();

    private Long id;
    private String email;

    private Integer age;

    public LoginMember() {
    }

    public LoginMember(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(String email, int age) {
        return new LoginMember(null, email, age);
    }

    public static LoginMember from() {
        return GUEST;
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
        return this == GUEST;
    }
}
