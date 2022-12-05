package nextstep.subway.fare.policy;

import java.util.Arrays;
import java.util.function.BiFunction;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

public enum DistanceSurcharge {
    BASIC_SECTION(0, 10, 0, 0, (distance, distanceSurcharge) -> 0),
    FIRST_SECTION(11, 50, 5, 0, DistanceSurcharge::calculateSurcharge),
    SECOND_SECTION(51, Integer.MAX_VALUE, 8, 800, DistanceSurcharge::calculateSurcharge);

    public static final String ERROR_MESSAGE_INVALID_PATH_DISTANCE = "잘못된 경로입니다.";
    public static final Fare BASIC_FARE = Fare.from(1250);
    private final int start;
    private final int end;
    private final int interval;
    private final Fare previousSectionSurcharge;
    private final BiFunction<Distance, DistanceSurcharge, Integer> calculation;

    DistanceSurcharge(int start, int end, int interval, int previousSectionSurcharge, BiFunction<Distance, DistanceSurcharge, Integer> calculation) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.previousSectionSurcharge = Fare.from(previousSectionSurcharge);
        this.calculation = calculation;
    }

    public static DistanceSurcharge from(Distance distance) {
        return Arrays.stream(values())
                .filter(distanceFare -> distanceFare.between(distance.value()))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException(ERROR_MESSAGE_INVALID_PATH_DISTANCE));
    }

    private boolean between(int distance) {
        return start <= distance && distance <= end;
    }

    private static int calculateSurcharge(Distance distance, DistanceSurcharge distanceSurcharge) {
        return (int) ((Math.ceil((distance.value() - distanceSurcharge.start - 1) / distanceSurcharge.interval) + 1) * 100);
    }

    public static Fare calculate(Distance distance) {
        DistanceSurcharge distanceSurcharge = DistanceSurcharge.from(distance);
        return BASIC_FARE.add(Fare.from(distanceSurcharge.calculation.apply(distance, distanceSurcharge))).add(distanceSurcharge.previousSectionSurcharge);
    }
}
