package nextstep.subway.line.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.path.domain.DistanceFarePolicy;

import java.util.stream.Stream;

public enum DistanceFareKind {
    BASIC_DISTANCE_SECTION(0, 10, distance -> Fare.of(1250)),
    SECOND_DISTANCE_SECTION(10, 50, distance -> {
        int extraDistance = distance - BASIC_DISTANCE_SECTION.endDistance;
        return BASIC_DISTANCE_SECTION.sumDistanceFare(BASIC_DISTANCE_SECTION.endDistance).sum(calculateFare(extraDistance, 5));
    }),
    THIRD_DISTANCE_SECTION(50, Integer.MAX_VALUE,distance -> {
        int extraDistance = distance - SECOND_DISTANCE_SECTION.endDistance;
        return SECOND_DISTANCE_SECTION.sumDistanceFare(SECOND_DISTANCE_SECTION.endDistance).sum(calculateFare(extraDistance, 8));
    });

    private static final int EXTRA_FARE = 100;
    private final int startDistance;
    private final int endDistance;
    private final DistanceFarePolicy distanceFarePolicy;

    DistanceFareKind(int startDistance, int endDistance, DistanceFarePolicy distanceFarePolicy) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.distanceFarePolicy = distanceFarePolicy;
    }

    public static DistanceFareKind of(int distance) {
        return Stream.of(DistanceFareKind.values())
                .filter(it -> isMatchDistanceSection(distance, it))
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.IT_IS_WRONG_DISTANCE));
    }

    private static boolean isMatchDistanceSection(int distance, DistanceFareKind it) {
        return it.startDistance < distance && distance <= it.endDistance;
    }

    public Fare sumDistanceFare(int distance) {
        return distanceFarePolicy.calculate(distance);
    }

    private static Fare calculateFare(int extraDistance, int standardDistance){
        return new Fare((divideStandardDistance(extraDistance , standardDistance)) * EXTRA_FARE);
    }

    private static int divideStandardDistance(int extraDistance, int standardDistance){
        if(extraDistance / standardDistance < 1 ){
            return 1;
        }
        return extraDistance / standardDistance;
    }
}
