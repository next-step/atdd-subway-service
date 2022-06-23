package nextstep.subway.auth.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Age {
    private static final int MIN = 0;
    private final int value;

    public Age() {
        this(MIN);
    }

    public Age(final int value) {
        if (value < MIN) {
            throw new IllegalArgumentException("음수를 가질수 없다.");
        }
        this.value = value;
    }

    public int of() {
        return value;
    }

    public boolean isHigh(final Age target) {
        return target.isLowBy(value);
    }

    private boolean isLowBy(final int value) {
        return this.value < value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age1 = (Age) o;
        return value == age1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
