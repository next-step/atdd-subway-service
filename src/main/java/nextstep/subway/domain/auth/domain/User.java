package nextstep.subway.domain.auth.domain;

public abstract class User {

    protected static final int TEENAGER_MINIMUM_AGE = 13;
    protected static final int TEENAGER_MAXIMUM_AGE = 18;
    protected static final int CHILDREN_MINIMUM_AGE = 6;
    protected static final int CHILDREN_MAXIMUM_AGE = 12;

    protected Long id;
    protected String email;
    protected Integer age;

    protected User() {
    }

    protected User(final Integer age) {
        this.age = age;
    }

    protected User(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public abstract boolean isChildren();

    public abstract boolean isTeenager();

    public abstract Long getId();

    public abstract String getEmail();

    public abstract Integer getAge();

    public abstract boolean isLoginUser();
}
