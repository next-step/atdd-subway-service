package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.ExceedCharge.FIFTY_EXCEED;
import static nextstep.subway.line.domain.ExceedCharge.TEN_EXCEED;

public class OperationCostPolicy {
    private static final long ZERO = 0;
    private static final long DEFAULT_PRICE = 1250;
    private static final long NO_EXCEED_DISTANCE = 10;
    private static final long LAST_EXCEED_DISTANCE = 50;


    public static Price basicDistancePrice() {
        return new Price(DEFAULT_PRICE);
    }

    public static Price overDistancePrice(final long distance) {
        if (distance <= NO_EXCEED_DISTANCE) {
            return new Price(ZERO);
        }
        final long lastExceedDistance = Math.max(distance - LAST_EXCEED_DISTANCE, ZERO);
        final Price tenExceedCharge = TEN_EXCEED.calculateOverFare(distance - NO_EXCEED_DISTANCE - lastExceedDistance);
        return tenExceedCharge.plus(FIFTY_EXCEED.calculateOverFare(lastExceedDistance));
    }


}
