package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static nextstep.subway.path.domain.FareByDistancePolicy.*;

public class FareCalculator {

    public static int calculate(Sections sections, Path path, DiscountPolicy policy) {
        int fare = calculateByDistance(path.getDistance());
        fare += calculateByAdditionalFareOfLine(sections, path.getStationPaths());
        fare = policy.discount(fare);

        return fare;
    }

    public static int calculateByDistance(int distance) {
        int fareByDistance = DEFAULT_FARE;

        if (distance > DEFAULT_FARE_SECTION_DISTANCE && distance <= EXCESS_FARE_SECTION_DISTANCE) {
            fareByDistance += calculateOverFare(distance - DEFAULT_FARE_SECTION_DISTANCE, UNIT_MID_DISTANCE);
        }

        if (distance > EXCESS_FARE_SECTION_DISTANCE) {
            fareByDistance += calculateOverFare(EXCESS_FARE_SECTION_DISTANCE - DEFAULT_FARE_SECTION_DISTANCE, UNIT_MID_DISTANCE);
            fareByDistance += calculateOverFare(distance - EXCESS_FARE_SECTION_DISTANCE, UNIT_LONG_DISTANCE);
        }

        return fareByDistance;
    }

    private static int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - CORRECTION_VALUE) / unitDistance) + CORRECTION_VALUE) * FARE_PER_UNIT_DISTANCE);
    }

    public static int calculateByAdditionalFareOfLine(Sections sections, List<Station> stationPaths) {
        return sliding(stationPaths, 2)
                .map(subList -> sections.getLineByStations(subList.get(0), subList.get(1)))
                .mapToInt(line -> line.getAdditionalFare())
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    private static <Station> Stream<List<Station>> sliding(List<Station> paths, int slidingWindowSize) {
        if (slidingWindowSize > paths.size()) {
            return Stream.empty();
        } else {
            return IntStream.range(0, paths.size() - slidingWindowSize + 1)
                    .mapToObj(start -> paths.subList(start, start + slidingWindowSize));
        }
    }
}
