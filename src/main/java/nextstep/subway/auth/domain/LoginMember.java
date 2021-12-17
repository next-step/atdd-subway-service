package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Age age = new Age();

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.of(age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }
}
