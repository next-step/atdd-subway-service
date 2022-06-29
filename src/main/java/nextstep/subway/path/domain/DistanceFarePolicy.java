package nextstep.subway.path.domain;

public enum DistanceFarePolicy {
    THRESHOLD_OF_TEN_KILOMETERS(10),
    DISTANCE_UNIT_AFTER_TEN(5),

    THRESHOLD_OF_FIFTY_KILOMETERS(50),
    DISTANCE_UNIT_AFTER_FIFTY(8),

    EXTRA_CHARGE_BY_DISTANCE_UNIT(100),
    EXTRA_CHARGE_FROM_TEN_TO_FIFTY(800),
    ;

    private int value;

    DistanceFarePolicy(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static boolean isLongerThanTenAndLessThanFiftyKilometers(final Path path) {
        final int distance = path.getDistance();
        return distance > THRESHOLD_OF_TEN_KILOMETERS.value() && distance <= THRESHOLD_OF_FIFTY_KILOMETERS.value();
    }

    public static boolean isLongerThanFiftyKilometers(final Path path) {
        final int distance = path.getDistance();
        return distance > DistanceFarePolicy.THRESHOLD_OF_FIFTY_KILOMETERS.value();
    }
}
