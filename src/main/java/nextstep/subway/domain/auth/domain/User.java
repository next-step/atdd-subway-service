package nextstep.subway.domain.auth.domain;

public class User {
    private Long id;
    private String email;
    private Integer age;

    public User() {
    }

    public User(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public User(final Integer age) {
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
