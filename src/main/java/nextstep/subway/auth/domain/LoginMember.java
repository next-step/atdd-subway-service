package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean required;

    public LoginMember() {
        this.required = false;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.required = true;
    }

    public static LoginMember login(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
    }

    public static LoginMember guest() {
        return new LoginMember();
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

    public boolean isRequired() {
        return required;
    }
}
