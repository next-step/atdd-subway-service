package nextstep.subway.path.types;

public enum ChargeType {
    LESS_OR_EQUAL_TEN, BETWEEN_TEN_AND_FIFTY, MORE_OR_EQUAL_FIFTY;

    private static int TEN = 10;
    private static int FIFTY = 50;

    public static ChargeType of(final int distance) {
        if (distance <= TEN) {
            return LESS_OR_EQUAL_TEN;
        }
        if (TEN < distance && distance <= FIFTY) {
            return BETWEEN_TEN_AND_FIFTY;
        }
        return MORE_OR_EQUAL_FIFTY;
    }
}
