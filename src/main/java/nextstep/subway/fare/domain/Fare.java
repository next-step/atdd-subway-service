package nextstep.subway.fare.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Distance;

@Embeddable
public class Fare {

    private static final Fare BASIC_FARE = Fare.from(1250);
    private static final int ZERO = 0;

    @Column(nullable = false)
    private int fare;

    protected Fare() {}

    private Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public static Fare createFare(Distance distance) {
        Fare fare = createAdditionalFareOfDistance(distance);
        return fare.add(BASIC_FARE);
    }

    public static Fare createFare(AgeFarePolicy ageFarePolicy, Distance distance) {
        Fare fare = createAdditionalFareOfDistance(distance);
        return ageFarePolicy.calculateFare(fare.add(BASIC_FARE));
    }

    private static Fare createAdditionalFareOfDistance(Distance distance) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.findDistanceFarePolicy(distance);
        return distanceFarePolicy.calculateAdditionalFareOfDistance(distance);
    }

    private void validateFare(int fare) {
        if(fare < ZERO) {
            throw new IllegalArgumentException(ErrorCode.요금은_0보다_작을_수_없음.getErrorMessage());
        }
    }

    public Fare add(Fare fare) {
        return new Fare(this.fare + fare.fare);
    }

    public Fare subtract(Fare fare) {
        return new Fare(this.fare - fare.fare);
    }

    public Fare multiply(int count) {
        return new Fare(fare * count);
    }

    public Fare multiplyAndCeil(double percent) {
        return new Fare((int) Math.ceil((double) this.fare * percent));
    }

    public int value() {
        return this.fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
