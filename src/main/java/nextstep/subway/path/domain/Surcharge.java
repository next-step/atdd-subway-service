package nextstep.subway.path.domain;

import java.util.Objects;

public class Surcharge {
    public static final Surcharge ZERO = new Surcharge(0);
    private final int value;

    public Surcharge(int value) {
        this.value = value;
    }

    public Surcharge add(Surcharge surcharge) {
        return new Surcharge(this.value + surcharge.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Surcharge)) return false;
        Surcharge surcharge = (Surcharge) o;
        return value == surcharge.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
