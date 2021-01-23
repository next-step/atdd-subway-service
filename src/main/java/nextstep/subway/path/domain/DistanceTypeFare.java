package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

enum DistanceTypeFare {
    TEN_KM_OVER_FARE(distance -> distance > 10 && distance <= 50, distance -> (int) (Math.ceil((distance - 11) / 5) + 1) * 100),
    FIFTY_KM_OVER_FARE(distance -> distance > 50, distance -> (int) (((Math.ceil((distance - 51) / 8) + 1) * 100) + 800)),
    NO_OVER_FARE(distance -> distance <= 10, distance -> 0);

    private Function<Integer, Boolean> distanceType;
    private Function<Integer, Integer> experession;

    DistanceTypeFare(Function<Integer, Boolean> distanceType, Function<Integer, Integer> experession) {
        this.distanceType = distanceType;
        this.experession = experession;
    }

    public static DistanceTypeFare valueOf(int distance) {
        return Arrays.stream(DistanceTypeFare.values())
                .filter(it -> it.isDisTanceType(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isDisTanceType(int age) {
        return distanceType.apply(age);
    }

    public int calculate(int distance) {
        return experession.apply(distance);
    }
}
