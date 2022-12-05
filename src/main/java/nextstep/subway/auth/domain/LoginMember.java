package nextstep.subway.auth.domain;

public class LoginMember {
    private static final int MIN_AGE_CHILD = 6;
    private static final int MIN_AGE_TEEN = 13;
    private static final int MIN_AGE_ADULT = 19;

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

    public boolean isAdult() {
        return age >= MIN_AGE_ADULT;
    }

    public boolean isTeen() {
        return age >= MIN_AGE_TEEN;
    }

    public boolean isChild() {
        return age >= MIN_AGE_CHILD;
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
