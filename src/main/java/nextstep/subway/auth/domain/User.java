package nextstep.subway.auth.domain;

public class User {

    public static final int ADULT_MAX_AGE = 1_000;
    private Integer age = ADULT_MAX_AGE;

    public Integer getAge() {
        return age;
    }

}
