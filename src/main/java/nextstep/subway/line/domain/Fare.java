package nextstep.subway.line.domain;

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
        this.amount = DEFAULT_USE_FARE_AMOUNT;
    }

    public Fare(int amount) {
        this.amount = amount + DEFAULT_USE_FARE_AMOUNT;
    }

    public Fare gt(Fare fare) {
        if (this.amount > fare.amount) {
            return this;
        }
        return fare;
    }

    public int calculateTotalFare(int distance) {
        int overFare = calculateOverFare(distance);
        return amount + overFare;
    }

    private int calculateOverFare(int distance) {
        int overAmount = 0;
        if (DISTANCE_10_KM < distance) {
            overAmount += (int) ((Math.ceil((Math.min(distance, DISTANCE_50_KM) - DISTANCE_10_KM - 1) / 5) + 1) * 100);
        }
        if (DISTANCE_50_KM < distance) {
            overAmount += (int) ((Math.ceil((distance - DISTANCE_50_KM - 1) / 8) + 1) * 100);
        }
        return overAmount;
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
