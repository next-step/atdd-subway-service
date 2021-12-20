package nextstep.subway.domain.auth.domain;

public class User {

    private static final int TEENAGER_MINIMUM_AGE = 13;
    private static final int TEENAGER_MAXIMUM_AGE = 18;
    private static final int CHILDREN_MINIMUM_AGE = 6;
    private static final int CHILDREN_MAXIMUM_AGE = 12;

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

    public boolean isChildren() {
        return this.age >= CHILDREN_MINIMUM_AGE && this.age <= CHILDREN_MAXIMUM_AGE;
    }

    public boolean isTeenager() {
        return this.age >= TEENAGER_MINIMUM_AGE && this.age <= TEENAGER_MAXIMUM_AGE;
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

    public boolean isLoginUser() {
        return this instanceof LoginUser;
    }
}
