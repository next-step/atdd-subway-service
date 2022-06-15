package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;

public enum DistanceFarePolicy {
    BASIC_POLICY(DistanceFarePolicy::isInBasicFareRange, DistanceFarePolicy::calculateBasicFare),
    FIRST_ADDITIONAL_POLICY(DistanceFarePolicy::isInFirstAdditionalFareRange,
            DistanceFarePolicy::calculateFirstAdditionalFare),
    SECOND_ADDITIONAL_POLICY(DistanceFarePolicy::isInSecondAdditionalFareRange,
            DistanceFarePolicy::calculateSecondAdditionalFare);

    public static final int BASIC_END = 10;
    public static final int FIRST_ADDITION_END = 50;
    public static final int BASIC_FARE = 1_250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int FIRST_ADDITIONAL_UNIT = 5;
    public static final int SECOND_ADDITIONAL_UNIT = 8;
    private final Predicate<Distance> predicateByDistance;
    private final Function<Distance, Integer> calculateFare;

    DistanceFarePolicy(Predicate<Distance> predicateByDistance, Function<Distance, Integer> calculateFare) {
        this.predicateByDistance = predicateByDistance;
        this.calculateFare = calculateFare;
    }

    private static boolean isInBasicFareRange(Distance distance) {
        return distance.distance() <= BASIC_END;
    }

    private static boolean isInFirstAdditionalFareRange(Distance distance) {
        return distance.distance() > BASIC_END && distance.distance() <= FIRST_ADDITION_END;
    }

    private static boolean isInSecondAdditionalFareRange(Distance distance) {
        return distance.distance() > FIRST_ADDITION_END;
    }

    private static int calculateBasicFare(Distance distance) {
        return BASIC_FARE;
    }

    private static int calculateFirstAdditionalFare(Distance distance) {
        int previousFare = calculateBasicFare(Distance.valueOf(BASIC_END));
        int additionalFare = (int) ((Math.ceil((distance.distance() - BASIC_END - 1) / FIRST_ADDITIONAL_UNIT) + 1)
                * ADDITIONAL_FARE);
        return previousFare + additionalFare;
    }

    private static int calculateSecondAdditionalFare(Distance distance) {
        int previousFare = calculateFirstAdditionalFare(Distance.valueOf(FIRST_ADDITION_END));
        int additionalFare = (int) (
                (Math.ceil((distance.distance() - FIRST_ADDITION_END - 1) / SECOND_ADDITIONAL_UNIT) + 1)
                        * ADDITIONAL_FARE);
        return previousFare + additionalFare;
    }

    public static DistanceFarePolicy findDistanceFarePolicyByDistance(Distance distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.predicateByDistance
                        .test(distance))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("해당하는 거리 요금 정책이 없습니다."));
    }

    public int calculateFare(Distance distance) {
        return this.calculateFare.apply(distance);
    }
}
