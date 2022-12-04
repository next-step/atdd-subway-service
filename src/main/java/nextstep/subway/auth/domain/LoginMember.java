package nextstep.subway.auth.domain;

public class LoginMember {
    private final Long id;
    private final String email;
    private final Integer age;
    private final Long NON_MEMBER_ID = -1L;
    private final String NON_MEMBER_EMAIL = "";
    public static final Integer NON_MEMBER_AGE = -1;

    public LoginMember() {
        this.id = NON_MEMBER_ID;
        this.email = NON_MEMBER_EMAIL;
        this.age = NON_MEMBER_AGE;
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
