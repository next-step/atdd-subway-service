package nextstep.subway.path.types;

public enum ChargeType {
    LEVEL_ONE_CHARGE, LEVEL_TWO_CHARGE, DEFAULT_CHARGE;

    private static int TEN = 10;
    private static int FIFTY = 50;

    public static ChargeType of(final int distance) {
        if (distance <= TEN) {
            return DEFAULT_CHARGE;
        }
        if (TEN < distance && distance <= FIFTY) {
            return LEVEL_ONE_CHARGE;
        }
        return LEVEL_TWO_CHARGE;
    }
}
