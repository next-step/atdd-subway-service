package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public class OverFareCalculator {

    private static final int FIRST_OVER_FARE_RANGE_START = 10;
    private static final int FIRST_OVER_FARE_RANGE_END = 50;
    private static final int FIRST_OVER_FARE_RANGE_INTERVAL = 5;
    public static final int FIRST_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int SECOND_OVER_FARE_RANGE_START = 50;
    private static final int SECOND_OVER_FARE_RANGE_INTERVAL = 8;
    public static final int SECOND_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int NO_OVER_FARE = 0;

    public static int calculateOverFareByDistance(PathFindResult pathFindResult) {
        int distance = pathFindResult.getDistance();
        return calculateOverFareInFirstRange(distance)
                + calculateOverFareInSecondRange(distance);
    }

    private static int calculateOverFareInFirstRange(int distance) {
        int overDistance = Math.min(distance , FIRST_OVER_FARE_RANGE_END) - FIRST_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / FIRST_OVER_FARE_RANGE_INTERVAL) + 1) * FIRST_OVER_FARE_RANGE_FARE_UNIT);
    }

    private static boolean hasOverDistance(int overDistance){
        return overDistance > 0;
    }

    private static int calculateOverFareInSecondRange(int distance) {
        int overDistance = distance - SECOND_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_FARE_RANGE_INTERVAL) + 1) * SECOND_OVER_FARE_RANGE_FARE_UNIT);
    }

    public static int calculateOverFareByLine(PathFindResult pathFindResult) {
        Set<Line> lines = pathFindResult.getLines();
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .getAsInt();
    }

}
