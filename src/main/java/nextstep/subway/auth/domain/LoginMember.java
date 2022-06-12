package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    protected LoginMember() {
    }

    public LoginMember(Integer age) {
        this.age = age;
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
