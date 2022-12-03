package nextstep.subway.line.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;

@Embeddable
public class Fare implements Comparable<Fare> {

    private static final int ZERO = 0;
    private static final Fare BASIC_FARE = Fare.from(1250);
    private static final Fare ZERO_FARE = Fare.from(ZERO);

    @Column(nullable = false)
    private BigDecimal fare;

    protected Fare() {}

    private Fare(BigDecimal fare) {
        validateFare(fare);
        this.fare = fare;
    }

    public static Fare from(BigDecimal fare) {
        return new Fare(fare);
    }

    public static Fare from(int fare) {
        return new Fare(BigDecimal.valueOf(fare));
    }

    public static Fare createFare(Distance distance) {
        Fare fare = createAdditionalFareOfDistance(distance);
        return fare.add(BASIC_FARE);
    }

    public static Fare createFare(AgeFarePolicy ageFarePolicy, Distance distance) {
        Fare fare = createAdditionalFareOfDistance(distance);
        return ageFarePolicy.calculateFare(fare.add(BASIC_FARE));
    }

    public static Fare createFare(Fare maxLineFare, Distance distance) {
        return createAdditionalFareOfDistance(distance)
                .add(BASIC_FARE)
                .add(maxLineFare);
    }

    public static Fare findMaxLineFare(Set<Line> lines) {
        return lines.stream()
                .map(Line::getLineFare)
                .max(Fare::compareTo)
                .orElse(ZERO_FARE);
    }

    private static Fare createAdditionalFareOfDistance(Distance distance) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.findDistanceFarePolicy(distance);
        return distanceFarePolicy.calculateAdditionalFareOfDistance(distance);
    }

    private void validateFare(BigDecimal fare) {
        if(fare.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(ErrorCode.요금은_0보다_작을_수_없음.getErrorMessage());
        }
    }

    public Fare add(Fare fare) {
        return new Fare(this.fare.add(fare.fare));
    }

    public Fare subtract(Fare fare) {
        return new Fare(this.fare.subtract(fare.fare));
    }

    public Fare multiply(int count) {
        return new Fare(this.fare.multiply(BigDecimal.valueOf(count)));
    }

    public Fare multiplyAndCeil(double percent) {
        return new Fare(this.fare.multiply(BigDecimal.valueOf(percent)).setScale(0, RoundingMode.CEILING));
    }

    public BigDecimal value() {
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
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public int compareTo(Fare compareFare) {
        return this.fare.compareTo(compareFare.fare);
    }
}
