package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

import static nextstep.subway.path.domain.FareByDistancePolicy.*;

public class FareCalculator {

    public static int calculate(Sections sections, Path path, DiscountPolicy policy) {
        // 거리 운임 부과
        int fare = calculateByDistance(path.getDistance());
        // 노선 추가 요금 부과
//        fare += calculateByAdditionalFareOfLine(sections, path.getStationPaths());
        // 할인 정책 적용
        // fare = DiscountPolicy.discount(fare);
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

    private static int calculateByAdditionalFareOfLine(Sections sections, List<Station> stationPaths) {
        return 0;
    }

    private static boolean isCalculateOverFare(int overDistance) {
        return overDistance > 0;
    }

    private static int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - CORRECTION_VALUE) / unitDistance) + CORRECTION_VALUE) * FARE_PER_UNIT_DISTANCE);
    }
}
