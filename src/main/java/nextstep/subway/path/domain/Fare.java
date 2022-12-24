package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private static final long ZERO_FARE = 0L;
    private static final long BASE_FARE = 1_250L;
    private static final long ADD_FARE = 100L;
    private static final long DISCOUNT_FARE = 350L;
    private static final float DISCOUNT_RATE_CHILDREN = 0.5F;
    private static final float DISCOUNT_RATE_ADOLESCENT = 0.2F;
    private static final int FIRST_FARE_SECTION_DELIMITER = 10;
    private static final int SECOND_FARE_SECTION_DELIMITER = 50;
    private static final int FIRST_FARE_SECTION_PER_DISTANCE = 5;
    private static final int SECOND_FARE_SECTION_PER_DISTANCE = 8;

    @Column(nullable = false)
    private long fare;

    public Fare() {
        this.fare = ZERO_FARE;
    }

    private Fare(long fare) {
        if (isNegative(fare)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FARE_FORMAT.getErrorMessage());
        }
        this.fare = fare;
    }

    public static Fare from() {
        return new Fare();
    }

    public static Fare from(long fare) {
        return new Fare(fare);
    }

    public static Fare fromBaseFare() {
        return new Fare(BASE_FARE);
    }

    public static Fare fromBaseFare(long addFare) {
        return new Fare(BASE_FARE + addFare);
    }

    public long currentFare() {
        return this.fare;
    }

    public long currentFare(int distance, LoginMember member) {
        calculateFareByDistanceProportional(distance);

        return this.fare;
    }

    public void calculateFareByDistanceProportional(int distance) {
        if (isBelongToFirstFareSection(distance)) {
            fare += calculateOverFareWhenFirstFareSection(distance - FIRST_FARE_SECTION_DELIMITER);
        }
        if (isBelongToSecondFareSection(distance)) {
            fare += calculateOverFareWhenSecondFareSection(distance - SECOND_FARE_SECTION_DELIMITER);
        }
    }

    public long findFare() {
        return this.fare;
    }

    public void calculateDiscount(LoginMember member) {
        if (isAdolescent(member)) {
            this.fare -= DISCOUNT_FARE;
            this.fare -= this.fare * DISCOUNT_RATE_ADOLESCENT;
            return;
        }
        if (isChildren(member)) {
            this.fare -= DISCOUNT_FARE;
            this.fare -= this.fare * DISCOUNT_RATE_CHILDREN;
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

    private boolean isNegative(long fare) {
        return fare < 0;
    }

}
