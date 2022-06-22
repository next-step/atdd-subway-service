package nextstep.subway.auth.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {
    private static final int MIN = 0;
    private final int age;

    public Age() {
        this(MIN);
    }

    public Age(final int age) {
        if (age < MIN) {
            throw new IllegalArgumentException("음수를 가질수 없다.");
        }
        this.age = age;
    }

    public int of() {
        return age;
    }

    public boolean isHigh(final Age target) {
        return target.isLowBy(age);
    }

    private boolean isLowBy(final int age) {
        return this.age < age;
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
