package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;

import java.util.Objects;

public class Fare {

    private static final int DEFAULT_AMOUNT = 1250;
    private static final int EXTRA_AMOUNT = 100;
    private static final int DEFAULT_AMOUNT_DISTANCE = 10;
    private static final int ONE_STEP_EXTRA_FARE_MAX_DISTANCE = 50;
    private static final float ONE_STEP_EXTRA_FARE_DISTANCE = 5f;
    private static final float TWO_STEP_EXTRA_FARE_DISTANCE = 8f;

    private int amount;

    public Fare(final int amount) {
        if (amount < DEFAULT_AMOUNT) {
            throw new IllegalArgumentException(String.format("요금은 %d 이상입니다.", DEFAULT_AMOUNT));
        }
        this.amount = amount;
    }

    public Fare(final Distance distance) {
        this(DEFAULT_AMOUNT);
        final int dt = distance.getDistance();
        if (isExtraDistance(dt)) {
            this.amount += extraAmount(dt);
        }
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
