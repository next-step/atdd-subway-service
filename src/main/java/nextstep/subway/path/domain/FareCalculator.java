package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.springframework.stereotype.Component;

public interface FareCalculator {
    boolean canCalculate(long distance);

    Fare calculate(long distance);
}


@Component
class Within10kmFareCalculator implements FareCalculator {
    static final int UPPER_BOUND = 10;
    static final int MAX = 1250;

    @Override
    public boolean canCalculate(long distance) {
        return distance < UPPER_BOUND;
    }

    @Override
    public Fare calculate(long distance) {
        return new Fare(MAX);
    }
}


@Component
class Between10kmAnd50kmFareCalculator implements FareCalculator {
    static final int UPPER_BOUND = 50;
    static final int KM_DISTANCE_UNIT = 5;
    static final int EXCEED_FARE = 100;
    static final int MAX = 2050;

    @Override
    public boolean canCalculate(long distance) {
        return distance >= Within10kmFareCalculator.UPPER_BOUND && distance <= UPPER_BOUND;
    }

    @Override
    public Fare calculate(long distance) {
        long surplus = distance - Within10kmFareCalculator.UPPER_BOUND;
        return new Fare(Within10kmFareCalculator.MAX + (long) ((Math.ceil((surplus - 1) / KM_DISTANCE_UNIT) + 1)
                * EXCEED_FARE));
    }
}


@Component
class Over50kmFareCalculator implements FareCalculator {
    static final int KM_DISTANCE_UNIT = 8;
    static final int EXCEED_FARE = 100;

    @Override
    public boolean canCalculate(long distance) {
        return distance > Between10kmAnd50kmFareCalculator.UPPER_BOUND;
    }

    @Override
    public Fare calculate(long distance) {
        long surplus = distance - Between10kmAnd50kmFareCalculator.UPPER_BOUND;
        return new Fare(Between10kmAnd50kmFareCalculator.MAX + (long) ((Math.ceil((surplus - 1) / KM_DISTANCE_UNIT) + 1)
                * EXCEED_FARE));
    }
}
