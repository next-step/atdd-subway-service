package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public class DefaultSubwayFarePolicy implements SubwayFarePolicy {
    @Override
    public int calculateOverFareByDistance(int distance) {
        return calculateOverFareInFirstRange(distance)
                + calculateOverFareInSecondRange(distance);
    }

    private int calculateOverFareInFirstRange(int distance) {
        int overDistance = Math.min(distance, FIRST_OVER_FARE_RANGE_END) - FIRST_OVER_FARE_RANGE_START;
        if (!hasOverDistance(overDistance)) {
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / FIRST_OVER_FARE_RANGE_INTERVAL) + 1)
                * FIRST_OVER_FARE_RANGE_FARE_UNIT);
    }

    private boolean hasOverDistance(int overDistance) {
        return overDistance > 0;
    }

    private int calculateOverFareInSecondRange(int distance) {
        int overDistance = distance - SECOND_OVER_FARE_RANGE_START;
        if (!hasOverDistance(overDistance)) {
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_FARE_RANGE_INTERVAL) + 1)
                * SECOND_OVER_FARE_RANGE_FARE_UNIT);
    }

    @Override
    public int calculateOverFareByLine(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .getAsInt();
    }

    @Override
    public SubwayFare discountFareByAge(SubwayFare beforeFare, SubwayUser subwayUser) {
        return subwayUser.discountFareByAge(beforeFare);
    }
}
