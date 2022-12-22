package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    private double fare;

    protected Fare() {
        this.fare = 0;
    }

    public Fare(double fare) {
        this.fare = fare;
    }

    public Fare(PathDistance pathDistance) {
        fare = 1250;
        if (checkMinimumFareDistanceExceeded()) {
            fare += calculateOverFare(pathDistance);
        }
    }

    public boolean checkMinimumFareDistanceExceeded() {
        return fare > 10;
    }

    private int calculateOverFare(PathDistance pathDistance) {
        int overFareFactor = pathDistance.getOverFareFactor();

        return overFareFactor * 100;
    }

    public Fare subtract(Fare fareToSubtract) {
        return new Fare(this.fare - fareToSubtract.fare);
    }

    public Fare subtract(double fareToSubtract) {
        return this.subtract(new Fare(fareToSubtract));
    }

    public Fare getDiscountFareByAge(Integer age) {
        if (age >= 6 && age < 13) {
            return this.discountByPercentage(50);
        }
        if (age >= 13 && age < 19) {
            return this.discountByPercentage(20);
        }
        return new Fare();
    }

    private Fare discountByPercentage(int percentage) {
        return new Fare(350 + Math.floor((fare - 350) * percentage / 100));
    }

    public double getFare() {
        return fare;
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
        return Double.compare(fare1.fare, fare) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
