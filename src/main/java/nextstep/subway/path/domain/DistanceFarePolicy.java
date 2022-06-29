package nextstep.subway.path.domain;

public enum DistanceFarePolicy {
    DISTANCE_THRESHOLD_AFTER_TEN(10),
    DISTANCE_UNIT_AFTER_TEN(5),

    DISTANCE_THRESHOLD_AFTER_FIFTY(50),
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
        return distance > DISTANCE_THRESHOLD_AFTER_TEN.value() && distance <= DISTANCE_THRESHOLD_AFTER_FIFTY.value();
    }

    public static boolean isLongerThanFiftyKilometers(final Path path) {
        final int distance = path.getDistance();
        return distance > DistanceFarePolicy.DISTANCE_THRESHOLD_AFTER_FIFTY.value();
    }
}
