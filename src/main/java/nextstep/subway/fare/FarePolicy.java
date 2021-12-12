package nextstep.subway.fare;

import nextstep.subway.line.domain.Lines;

public class FarePolicy {

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE_BY_DISTANCE = 100;
    public static final int BASIC_DISTANCE = 10;
    public static final int TEN_KM = 10;
    public static final int FIFTY_KM = 50;
    public static final int FIVE_KM = 5;
    public static final int EIGHT_KM = 8;

    private FarePolicy() {
    }

    public static Integer getFare(Lines lines, Integer distance) {
        return fareByDistance(distance) + additionalFareByLine(lines);
    }

    public static Integer fareByDistance(Integer distance) {
        if (distance <= TEN_KM) {
            return BASIC_FARE;
        }

        if (distance > FIFTY_KM) {
            return BASIC_FARE + calculateOverFare(FIFTY_KM - BASIC_DISTANCE, FIVE_KM)
                + calculateOverFare(distance - FIFTY_KM, EIGHT_KM);
        }

        return BASIC_FARE + calculateOverFare(distance - BASIC_DISTANCE, FIVE_KM);
    }

    public static Integer additionalFareByLine(Lines lines) {
        return lines.mostExpensiveLine()
            .getAdditionalFare();
    }

    private static int calculateOverFare(int distance, int distanceForOverFare) {
        return (int) ((Math.ceil((distance - 1) / distanceForOverFare) + 1)
            * OVER_FARE_BY_DISTANCE);
    }
}
