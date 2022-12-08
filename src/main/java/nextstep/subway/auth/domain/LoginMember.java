package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {
    private static final String GUEST_EMAIL = "GUEST";
    private static final int GUEST_AGE = 20;
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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public static LoginMember guest() {
        return new LoginMember(null, GUEST_EMAIL, GUEST_AGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginMember)) return false;
        LoginMember that = (LoginMember) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
