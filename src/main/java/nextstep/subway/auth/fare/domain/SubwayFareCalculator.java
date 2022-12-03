package nextstep.subway.auth.fare.domain;

import nextstep.subway.common.exception.ErrorEnum;

public class SubwayFareCalculator {
    public static final int ZERO_DISTANCE = 0;
    public static final int BASIC_FARE = 1_250;
    public static final int MAX_DISTANCE_OF_BASIC_FARE = 10;

    public static int calculate(int distance) {
        if (distance <= ZERO_DISTANCE) {
            throw new IllegalArgumentException(ErrorEnum.DISTANCE_GREATER_ZERO.message());
        }
        if (distance <= MAX_DISTANCE_OF_BASIC_FARE) {
            return BASIC_FARE;
        }
        return 0;
    }
}
