package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.DiscountByAgeCriterion.checkDiscountByAgeCriterion;

import java.util.Objects;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.ExtraFare;

public class Fare {

    public static final int DO_NOT_DISCOUNT = 0;
    public static final double DEFAULT_DISCOUNT_ZERO = 0.0;
    private static final Double DEFAULT_DISCOUNT_AMOUNT = 350.0;

    private Double fare;

    protected Fare() {
        this.fare = (double) 0;
    }

    public Fare(double fare) {
        this.fare = fare;
    }

    public Fare(PathDistance pathDistance) {
        fare = (double) 1250;
        fare += calculateOverFare(pathDistance);
    }

    private int calculateOverFare(PathDistance pathDistance) {
        int overFareFactor = pathDistance.getOverFareFactor();
        return overFareFactor * 100;
    }

    public Fare subtract(Fare fareToSubtract) {
        return new Fare(this.fare - fareToSubtract.fare);
    }

    public Fare applyAgeDiscount(LoginMember loginMember) {
        Integer age = loginMember.getAge();
        Fare fareToDiscount = new Fare();
        if (age != null) {
            fareToDiscount = getDiscountFareByAge(age);
        }
        return this.subtract(fareToDiscount);
    }

    public Fare getDiscountFareByAge(Integer age) {
        DiscountByAgeCriterion discountByAgeCriterion = checkDiscountByAgeCriterion(age);
        return calculateDiscountFareByFactor(discountByAgeCriterion.getDiscountFactor());
    }

    private Fare calculateDiscountFareByFactor(double factor) {
        Double defaultDiscountAmount = DEFAULT_DISCOUNT_AMOUNT;
        if (factor == DO_NOT_DISCOUNT) {
            defaultDiscountAmount = DEFAULT_DISCOUNT_ZERO;
        }
        return new Fare(defaultDiscountAmount + Math.floor((fare - defaultDiscountAmount) * factor));
    }

    public Fare add(ExtraFare extraFare) {
        return new Fare(fare + extraFare.getExtraFare());
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
}
