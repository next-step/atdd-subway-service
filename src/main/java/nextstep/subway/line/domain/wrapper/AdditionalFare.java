package nextstep.subway.line.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AdditionalFare {
    private static final int INIT_FARE = 0;

    @Column(name = "fare", nullable = false)
    private int fare;

    protected AdditionalFare() {
    }

    private AdditionalFare(final int fare) {
        this.fare = fare;
    }

    public static AdditionalFare from(final int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("추가요금은 0원보다 작을 수 없습니다.");
        }
        return new AdditionalFare(fare);
    }

    public static AdditionalFare newInstance() {
        return new AdditionalFare(INIT_FARE);
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
