package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.fomular.OverFare;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {
    public static final int DEFAULT_USE_FARE_AMOUNT = 1250;
    public static final int DISTANCE_10_KM = 10;
    public static final int DISTANCE_50_KM = 50;

    @Column(name = "fare")
    private final int amount;

    public Fare() {
        this.amount = 0;
    }

    public Fare(int amount) {
        this.amount = amount;
    }

    public Fare gt(Fare fare) {
        if (this.amount > fare.amount) {
            return this;
        }
        return fare;
    }

    public Fare calculateTotalFare(Distance distance) {
        Fare overFare = calculateOverFare(distance);
        return new Fare(amount + overFare.amount);
    }

    private Fare calculateOverFare(Distance distance) {
        int overAmount = DEFAULT_USE_FARE_AMOUNT;
        int distanceToInt = distance.toInt();
        if (distance.isOver(DISTANCE_10_KM)) {
            overAmount += OverFare.DISTANCE_10_KM.calculateOverFare((Math.min(distanceToInt, DISTANCE_50_KM)));
        }
        if (distance.isOver(DISTANCE_50_KM)) {
            overAmount += OverFare.DISTANCE_50_KM.calculateOverFare(distanceToInt);
        }
        return new Fare(overAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public int amount() {
        return amount;
    }
}
