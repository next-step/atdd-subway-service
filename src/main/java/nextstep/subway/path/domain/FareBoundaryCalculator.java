package nextstep.subway.path.domain;

import nextstep.subway.path.domain.interfaces.FareCalculator;

public class FareBoundaryCalculator implements FareCalculator {

    private static final int OVER_FARE_DISTANCE = 5;

    @Override
    public int calcOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / OVER_FARE_DISTANCE) + 1) * 100);
    }
}
