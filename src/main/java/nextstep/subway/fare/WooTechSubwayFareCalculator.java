package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;

import static nextstep.subway.fare.FareConstants.ADD_FARE;
import static nextstep.subway.fare.FareConstants.DISCOUNT_FARE;
import static nextstep.subway.fare.FareConstants.DISCOUNT_RATE_ADOLESCENT;
import static nextstep.subway.fare.FareConstants.DISCOUNT_RATE_CHILDREN;
import static nextstep.subway.fare.FareConstants.FIRST_FARE_SECTION_DELIMITER;
import static nextstep.subway.fare.FareConstants.FIRST_FARE_SECTION_PER_DISTANCE;
import static nextstep.subway.fare.FareConstants.SECOND_FARE_SECTION_DELIMITER;
import static nextstep.subway.fare.FareConstants.SECOND_FARE_SECTION_PER_DISTANCE;

public class WooTechSubwayFareCalculator implements FareCalculator{

    private long fare;

    public WooTechSubwayFareCalculator(long fare) {
        this.fare = fare;
    }

    @Override
    public long fareCalculate(int distance, LoginMember member) {
        calculateFareByDistanceProportional(distance);
        calculateDiscount(member);

        return 0;
    }

    public void calculateFareByDistanceProportional(int distance) {
        if (isBelongToFirstFareSection(distance)) {
            fare += calculateOverFareWhenFirstFareSection(distance - FIRST_FARE_SECTION_DELIMITER);
        }
        if (isBelongToSecondFareSection(distance)) {
            fare += calculateOverFareWhenSecondFareSection(distance - SECOND_FARE_SECTION_DELIMITER);
        }
    }

    public void calculateDiscount(LoginMember member) {
        if (isAdolescent(member)) {
            this.fare -= DISCOUNT_FARE;
            this.fare -= this.fare * DISCOUNT_RATE_ADOLESCENT;
            checkValidation(fare);
            return;
        }
        if (isChildren(member)) {
            this.fare -= DISCOUNT_FARE;
            this.fare -= this.fare * DISCOUNT_RATE_CHILDREN;
            checkValidation(fare);
            return;
        }
    }

    private boolean isBelongToSecondFareSection(int distance) {
        return distance > SECOND_FARE_SECTION_DELIMITER;
    }

    private boolean isBelongToFirstFareSection(int distance) {
        return distance > FIRST_FARE_SECTION_DELIMITER;
    }

    private long calculateOverFareWhenFirstFareSection(int distance) {
        if (isBelongToSecondFareSection(distance + FIRST_FARE_SECTION_DELIMITER)) {
            distance = SECOND_FARE_SECTION_DELIMITER - FIRST_FARE_SECTION_DELIMITER;
        }
        return (int) ((Math.ceil((distance - 1) / FIRST_FARE_SECTION_PER_DISTANCE) + 1) * ADD_FARE);
    }

    private long calculateOverFareWhenSecondFareSection(int distance) {
        return (int) ((Math.ceil((distance - 1) / SECOND_FARE_SECTION_PER_DISTANCE) + 1) * ADD_FARE);
    }

    private boolean isChildren(LoginMember member) {
        return member.getAge() < 13 && member.getAge() >= 6;
    }

    private boolean isAdolescent(LoginMember member) {
        return member.getAge() < 19 && member.getAge() >= 13;
    }

    private void checkValidation(long fare) {
        if (isNegative(fare)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FARE_FORMAT.getErrorMessage());
        }
    }

    private boolean isNegative(long fare) {
        return fare < 0;
    }
}
