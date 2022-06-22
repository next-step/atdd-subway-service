package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public interface SubwayFarePolicy {
    int FIRST_OVER_FARE_RANGE_START = 10;
    int FIRST_OVER_FARE_RANGE_END = 50;
    int FIRST_OVER_FARE_RANGE_INTERVAL = 5;
    int FIRST_OVER_FARE_RANGE_FARE_UNIT = 100;

    int SECOND_OVER_FARE_RANGE_START = 50;
    int SECOND_OVER_FARE_RANGE_INTERVAL = 8;
    int SECOND_OVER_FARE_RANGE_FARE_UNIT = 100;

    int NO_OVER_FARE = 0;

    int calculateOverFareByDistance(int distance);

    int calculateOverFareByLine(Set<Line> lines);

    SubwayFare discountFareByAge(SubwayFare beforeFare, SubwayUser subwayUser);
}
