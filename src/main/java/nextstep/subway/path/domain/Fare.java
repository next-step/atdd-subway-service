package nextstep.subway.path.domain;

import java.util.Objects;
import nextstep.subway.line.domain.ExtraFare;

public class Fare {

    private Double fare;

    protected Fare() {
        this.fare = (double) 0;
    }

    public Fare(double fare) {
        this.fare = fare;
    }

    public Fare(PathDistance pathDistance) {
        fare = (double) 1250;
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

    public Fare applyAgeDiscount(Integer age) {
        Fare fareToDiscount = getDiscountFareByAge(age);
        return this.subtract(fareToDiscount);
    }

    public Fare getDiscountFareByAge(Integer age) {
        if (age >= 6 && age < 13) {
            return discountByPercentage(50);
        }
        if (age >= 13 && age < 19) {
            return discountByPercentage(20);
        }
        return new Fare();
    }

    private Fare discountByPercentage(int percentage) {
        return new Fare(350 + Math.floor((fare - 350) * percentage / 100));
    }

    public Double getFare() {
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

    public Fare add(ExtraFare extraFare) {
        return new Fare(fare + extraFare.getExtraFare());
    }
}
