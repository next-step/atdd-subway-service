package nextstep.subway.auth.domain;

public class LoginMember {
    private static final Long GUEST_ID = -1L;
    private static final Integer GUEST_AGE = 0;

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
        this.id = GUEST_ID;
        this.age = GUEST_AGE;
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
}
