package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int MIN_FARE = 0;

    private int fare;

    protected Fare() {
    }

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int fare) {
        validate(fare);
        return new Fare(DEFAULT_FARE + fare);
    }

    public static Fare init() {
        return new Fare(DEFAULT_FARE);
    }

    private static void validate(int fare) {
        if (fare < MIN_FARE) {
            throw new IllegalArgumentException("유효하지 않은 운임비용입니다.");
        }
    }

    public int toNumber() {
        return this.fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fare)) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
