package nextstep.subway.auth.domain;

import java.util.Objects;

public class Age {
    private static final int MIN = 0;
    private final int age;

    public Age(final int age) {
        if (age < MIN) {
            throw new IllegalArgumentException("음수를 가질수 없다.");
        }
        this.age = age;
    }

    public int of() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age1 = (Age) o;
        return age == age1.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
