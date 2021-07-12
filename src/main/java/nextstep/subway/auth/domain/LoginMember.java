package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private LoginStatus status;

    public LoginMember() {
        this.status = LoginStatus.NON_LOGIN;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.status = LoginStatus.LOGIN;
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

    public LoginStatus getStatus() {
        return status;
    }
}
