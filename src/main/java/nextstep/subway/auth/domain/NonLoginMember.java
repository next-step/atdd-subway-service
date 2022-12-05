package nextstep.subway.auth.domain;

public class NonLoginMember extends LoginMember {
    private final Long id;
    private final String email;
    private final Integer age;
    public static int NON_MEMBER_AGE = -1;


    public NonLoginMember() {
        this.id = -1L;
        this.email = "";
        this.age = NON_MEMBER_AGE;
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
