package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class DistancePolicy {

    private static final int BASIC_DISTANCE = 10;
    private static final int OVER_DISTANCE = 50;
    private static final int BASIC_FARE = 1_250;
    private static final int OVER_FARE_STANDARD_1 = 5;
    private static final int OVER_FARE_STANDARD_2 = 8;

    public static int getDistanceFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance <= OVER_DISTANCE) {
            return BASIC_FARE + calculateDistanceFare(distance - BASIC_DISTANCE, OVER_FARE_STANDARD_1);
        }

        return BASIC_FARE + calculateDistanceFare(OVER_DISTANCE - BASIC_DISTANCE, OVER_FARE_STANDARD_1) +
                calculateDistanceFare(distance - OVER_DISTANCE, OVER_FARE_STANDARD_2);
    }

    public static int getLineAdditionalFare(List<Station> path, Sections sections) {
        int maxFare = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int currentFare = sections.getLineAdditionalFareByUpAndDownStation(path.get(i), path.get(i + 1));
            maxFare = Math.max(maxFare, currentFare);
        }
        return maxFare;
    }

    private static int calculateDistanceFare(int distance, int eachStandard) {
        return (int) ((Math.ceil((distance - 1) / eachStandard) + 1) * 100);
    }

}
