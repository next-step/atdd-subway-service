package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {

    private static final Long GUEST_ID = 0L;

    public static final LoginMember GUEST = new LoginMember(GUEST_ID, "", 0);

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

    public boolean isGuest() {
        return Objects.equals(id, GUEST_ID);
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
