package nextstep.subway.path.domain;

public class Age {

    private static final double CHILD_AGE_MIN = 6;
    private static final double TEEN_AGE_MIN = 13;
    private static final double TEEN_AGE_MAX = 18;
    private final int value;

    public Age(final int age) {
        this.value = age;
    }

    public boolean isChild() {
        return value >= CHILD_AGE_MIN && value < TEEN_AGE_MIN;
    }

    public boolean isTeen() {
        return value >= TEEN_AGE_MIN && value <= TEEN_AGE_MAX;
    }
}
