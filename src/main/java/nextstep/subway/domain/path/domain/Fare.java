package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.LoginMember;
import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;

import java.util.List;
import java.util.Objects;

public class Fare {

    private static final int DEFAULT_AMOUNT = 1250;
    private static final int EXTRA_AMOUNT = 100;
    private static final int DEFAULT_AMOUNT_DISTANCE = 10;
    private static final int ONE_STEP_EXTRA_FARE_MAX_DISTANCE = 50;
    private static final float ONE_STEP_EXTRA_FARE_DISTANCE = 5f;
    private static final float TWO_STEP_EXTRA_FARE_DISTANCE = 8f;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    private int amount;

    public Fare(final int amount) {
        this.amount = amount;
    }

    public Fare(final Distance distance) {
        this(DEFAULT_AMOUNT);
        final int dt = distance.getDistance();
        if (isExtraDistance(dt)) {
            this.amount += extraAmount(dt);
        }
    }

    public Fare(final Distance distance, List<Line> lines) {
        this(distance);
        final int lineMaxExtraFare = lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();
        this.amount += lineMaxExtraFare;
    }

    public Fare(final Distance distance, List<Line> lines, LoginMember loginMember) {
        this(distance, lines);
        if (isTeenager(loginMember)) {
            this.amount = discountAmountByAge(TEENAGER_DISCOUNT_RATE);
            return;
        }
        if (isChildren(loginMember)) {
            this.amount = discountAmountByAge(CHILDREN_DISCOUNT_RATE);
        }
    }

    private int discountAmountByAge(double discountRate) {
        return (int) ((this.amount - 350) * (1-discountRate));
    }

    private boolean isChildren(final LoginMember loginMember) {
        return loginMember.getAge() >= 6 && loginMember.getAge() < 13;
    }

    private boolean isTeenager(final LoginMember loginMember) {
        return loginMember.getAge() >= 13 && loginMember.getAge() < 19;
    }

    private boolean isExtraDistance(int distance) {
        return distance > DEFAULT_AMOUNT_DISTANCE;
    }

    private int extraAmount(final int distance) {
        int extraAmount = extraAmount(stepOneExcessDistance(distance), ONE_STEP_EXTRA_FARE_DISTANCE);

        if (isStepTwoExcessDistance(distance)) {
            extraAmount += extraAmount(stepTwoExcessDistance(distance), TWO_STEP_EXTRA_FARE_DISTANCE);
        }

        return extraAmount;
    }

    private int extraAmount(int excessDistance, float stepByStepExtraFareDistance) {
        return (int) (Math.ceil(excessDistance / stepByStepExtraFareDistance)) * EXTRA_AMOUNT;
    }

    private int stepOneExcessDistance(final int distance) {
        if (distance <= ONE_STEP_EXTRA_FARE_MAX_DISTANCE) {
            return distance - DEFAULT_AMOUNT_DISTANCE;
        }
        return ONE_STEP_EXTRA_FARE_MAX_DISTANCE - DEFAULT_AMOUNT_DISTANCE;
    }

    private boolean isStepTwoExcessDistance(final int distance) {
        return distance > ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    private int stepTwoExcessDistance(final int distance) {
        return distance - ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
