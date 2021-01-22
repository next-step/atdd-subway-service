package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;

public enum FareSection {
    BASIC(0, 10, 0, 0),
    MEDIUM(10, 50, 5, 100),
    LONG(50, Integer.MAX_VALUE, 8, 100);

    private final int minDistance;
    private final Optional<Integer> maxDistance;
    private final int chargingDistance;
    private final int additionalFare;

    FareSection(int minDistance, Integer maxDistance, int chargingDistance, int overFare) {
        this.minDistance = minDistance;
        this.maxDistance = Optional.ofNullable(maxDistance);
        this.chargingDistance = chargingDistance;
        this.additionalFare = overFare;
    }

    public static int calculateFareOf(int distance) {
        return valueOf(distance).calculateFare(distance);
    }

    private int calculateFare(int distance) {
        return BASIC_FARE.getValue() + Arrays.stream(FareSection.values())
                .filter(fare -> fare.compareTo(this) <= 0 && fare.chargingDistance != 0)
                .mapToInt(fare -> fare.calculateDistanceFare(distance))
                .sum();
    }

    private static FareSection valueOf(int distance) {
        return Arrays.stream(FareSection.values())
                .filter(fare -> fare.isBetweenMinAndMax(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("구간을 구할 수 없는 거리입니다."));
    }

    private boolean isBetweenMinAndMax(int distance) {
        return distance > minDistance && distance <= findMaxDistance(distance);
    }

    private int findDistanceInSection(int distance) {
        return Math.min(findMaxDistance(distance), distance) - minDistance;
    }

    private int findMaxDistance(int distance) {
        int max = distance;
        if (maxDistance.isPresent()) {
            max = maxDistance.get();
        }
        return max;
    }

    private int calculateDistanceFare(int distance) {
        int distanceInSection = findDistanceInSection(distance);
        return (int) ((Math.ceil((distanceInSection - 1) / chargingDistance) + 1) * additionalFare);
    }
}
