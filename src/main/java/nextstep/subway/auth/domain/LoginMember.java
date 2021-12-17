package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean isEmpty;

    public LoginMember() {
    }

    public LoginMember(boolean isEmpty) {
        this.isEmpty = isEmpty;
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

    public boolean isEmpty() {
        return isEmpty;
    }
}
