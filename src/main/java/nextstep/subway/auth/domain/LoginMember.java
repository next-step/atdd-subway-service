package nextstep.subway.auth.domain;

public class LoginMember implements AuthMember {
    private Long id;
    private String email;
    private int age;

    public LoginMember() {
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

    @Override
    public int getAge() {
        return age;
    }
}
