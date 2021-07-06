package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {
    private static final int ADULT_BASELINE_AGE = 19;
    private static final int TEENAGER_BASELINE_AGE = 13;
    private static final int CHILD_BASELINE_AGE = 6;
    private Long id;
    private String email;
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public boolean isLoginUser() {
        return !Objects.isNull(this.id) && this.id > 0;
    }

    public boolean isBaby() {
        return this.age < CHILD_BASELINE_AGE;
    }

    public boolean isChild() {
        return this.age < TEENAGER_BASELINE_AGE && this.age >= CHILD_BASELINE_AGE;
    }

    public boolean isTeenager() {
        return this.age < ADULT_BASELINE_AGE && this.age >= TEENAGER_BASELINE_AGE;
    }

    public boolean isAdult() {
        return this.age >= ADULT_BASELINE_AGE;
    }
}
