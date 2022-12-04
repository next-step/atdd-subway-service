package nextstep.subway.fare.policy;

import static nextstep.subway.fare.domain.Fare.FIRST_SECTION_MAX_SURCHARGE;
import static nextstep.subway.fare.domain.Fare.BASIC_SECTION_NO_SURCHARGE;

import java.util.Arrays;
import java.util.function.Function;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

public enum DistanceSurcharge {
    BASIC_SECTION(0, 10, 0, distance -> BASIC_SECTION_NO_SURCHARGE),
    FIRST_SECTION(10, 50, 5, DistanceSurcharge::calculateSurchargeFirstSection),
    SECOND_SECTION(50, Integer.MAX_VALUE, 8,
            distance -> FIRST_SECTION_MAX_SURCHARGE + calculateSurchargeSecondSection(distance));

    public static final String ERROR_MESSAGE_INVALID_PATH_DISTANCE = "잘못된 경로입니다.";
    private final int start;
    private final int end;
    private final int interval;
    private final Function<Distance, Integer> calculation;

    DistanceSurcharge(int start, int end, int interval, Function<Distance, Integer> calculation) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.calculation = calculation;
    }

    public static DistanceSurcharge from(Distance distance) {
        return Arrays.stream(values())
                .filter(distanceFare -> distanceFare.between(distance.value()))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException(ERROR_MESSAGE_INVALID_PATH_DISTANCE));
    }

    private boolean between(int distance) {
        return start < distance && distance <= end;
    }

    private static int calculateSurchargeFirstSection(Distance distance) {
        return (int) ((Math.ceil((distance.value() - FIRST_SECTION.start - 1) / FIRST_SECTION.interval) + 1) * 100);
    }

    private static int calculateSurchargeSecondSection(Distance distance) {
        return (int) ((Math.ceil((distance.value() - SECOND_SECTION.start - 1) / SECOND_SECTION.interval) + 1) * 100);
    }

    public static Fare calculate(Distance distance) {
        DistanceSurcharge distanceFare = DistanceSurcharge.from(distance);
        return Fare.from(distanceFare.calculation.apply(distance));
    }
}
