package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AdditionalFare {
    private static final int INITIAL_FARE = 0;

    @Column(nullable = false)
    private int fare;

    protected AdditionalFare() {
    }

    private AdditionalFare(int fare) {
        if (fare < INITIAL_FARE) {
            String message = String.format("추가 요금은 %d 보다 작을 수 없습니다.", INITIAL_FARE);
            throw new IllegalArgumentException(message);
        }
        this.fare = fare;
    }

    public static AdditionalFare from(int fare) {
        return new AdditionalFare(fare);
    }

    public static AdditionalFare init() {
        return new AdditionalFare(INITIAL_FARE);
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdditionalFare that = (AdditionalFare) o;
        return fare == that.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
