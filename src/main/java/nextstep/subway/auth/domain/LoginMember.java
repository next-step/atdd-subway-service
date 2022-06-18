package nextstep.subway.auth.domain;

public class LoginMember {
    private static final Long DEFAULT_ID = 0L;
    private static final String DEFAULT_EMAIL = "";
    private static final Integer DEFAULT_AGE = 0;

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
        this.id = DEFAULT_ID;
        this.email = DEFAULT_EMAIL;
        this.age = DEFAULT_AGE;
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

    public boolean isEqualDefaultValue() {
        return this.age.equals(DEFAULT_AGE) && this.email.equals(DEFAULT_EMAIL)
                && this.id.equals(DEFAULT_ID);
    }
}
