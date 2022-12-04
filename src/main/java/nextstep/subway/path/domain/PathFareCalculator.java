package nextstep.subway.path.domain;

public class PathFareCalculator {
    private static final Integer DEFAULT_FARE = 1250;
    private static final int MIDDLE_SURCHARGE_CONDITION = 5;
    private static final int LONG_SURCHARGE_CONDITION = 8;
    private static final int SURCHARGE = 100;

    public static Integer calculateDefaultFare() {
        return DEFAULT_FARE;
    }

    public static Integer calculateMiddleFare(int distance) {
        int fare = DEFAULT_FARE;
        int middleDistance = distance - PathFare.DEFAULT.getMaxDistance();
        fare += ((int) ((Math.ceil((middleDistance - 1) / MIDDLE_SURCHARGE_CONDITION) + 1) * SURCHARGE));

        return fare;
    }

    public static Integer calculateLongFare(int distance) {
        int fare = 0;
        int longDistance = distance - PathFare.MIDDLE.getMaxDistance();
        fare += calculateMiddleFare(PathFare.MIDDLE.getMaxDistance());
        fare += ((int) ((Math.ceil((longDistance - 1) / LONG_SURCHARGE_CONDITION) + 1) * SURCHARGE));

        return fare;
    }
}
