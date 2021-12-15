package nextstep.subway.path.domain.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.AgeType;
import nextstep.subway.path.domain.fare.policy.AdultDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.AgeDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.ChildDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.InfantDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.OverFarePolicy;
import nextstep.subway.path.domain.fare.policy.YouthDiscountPolicy;

public class Fare {

    public static final int ZERO = 0;
    static final Fare BASE_FARE = new Fare(1_250);

    private final int fare;

    Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare of(final Distance distance) {
        return OverFarePolicy.calculateFare(BASE_FARE, distance.getDistance());
    }

    public static Fare of(final Distance distance, final int additionalFare) {
        return OverFarePolicy.calculateFare(BASE_FARE, distance.getDistance())
            .add(additionalFare);
    }

    public static Fare of(final Distance distance, final int additionalFare,
        final LoginMember member) {

        AgeType ageType = AgeType.checkAgeType(member.getAge());
        AgeDiscountPolicy ageDiscountPolicy = createAgeDiscountPolicy(ageType);

        Fare fare = OverFarePolicy.calculateFare(BASE_FARE, distance.getDistance())
            .add(additionalFare);

        return ageDiscountPolicy.calculateFare(fare);
    }

    public Fare add(final int toAdd) {
        return new Fare(this.fare + toAdd);
    }

    public Fare subtract(final int toSubtract) {
        return new Fare(this.fare - toSubtract);
    }

    public Fare multiply(double discountRate) {
        return new Fare((int) (this.fare * discountRate));
    }

    public int getFare() {
        return fare;
    }

    private static AgeDiscountPolicy createAgeDiscountPolicy(AgeType ageType) {
        if (ageType == AgeType.INFANT) {
            return new InfantDiscountPolicy();
        }

        if (ageType == AgeType.CHILD) {
            return new ChildDiscountPolicy();
        }

        if (ageType == AgeType.YOUTH) {
            return new YouthDiscountPolicy();
        }

        return new AdultDiscountPolicy();
    }
}
