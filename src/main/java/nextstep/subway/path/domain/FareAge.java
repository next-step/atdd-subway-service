package nextstep.subway.path.domain;

import java.util.Objects;

public class FareAge {

    private static final int MINIMUM_AGE = 0;
    private static final int TEENAGE_START = 13;
    private static final int TEENAGE_END = 19;
    private static final int CHILD_START = 6;
    private static final int CHILD_END = 13;

    private final int age;

    public FareAge(final int age) {
        validate(age);
        this.age = age;
    }

    private void validate(final int age) {
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 " + MINIMUM_AGE + "보다 크거나 같아야 합니다.");
        }
    }

    public boolean isTeenager() {
        return age >= TEENAGE_START && age < TEENAGE_END;
    }

    public boolean isChild() {
        return age >= CHILD_START && age < CHILD_END;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FareAge fareAge = (FareAge) o;
        return age == fareAge.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
