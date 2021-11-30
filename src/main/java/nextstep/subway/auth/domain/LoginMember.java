package nextstep.subway.auth.domain;

public class LoginMember {

    private static final LoginMember GUEST = new LoginMember();
    private static final int CHILD_AGE_MIN = 6;
    private static final int CHILD_AGE_MAX = 13;
    private static final int YOUTH_AGE_MIN = 13;
    private static final int YOUTH_AGE_MAX = 19;

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

    public static LoginMember ofGuest() {
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

    public boolean isChild() {
        return age >= CHILD_AGE_MIN && age < CHILD_AGE_MAX;
    }

    public boolean isYouth() {
        return age >= YOUTH_AGE_MIN && age < YOUTH_AGE_MAX;
    }

}
