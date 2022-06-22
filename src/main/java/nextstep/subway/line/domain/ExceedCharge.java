package nextstep.subway.line.domain;

public enum ExceedCharge {
    TEN_EXCEED(5, 100), FIFTY_EXCEED(8,100);

    private static final long MIN_VALUE = 0;
    private final int unit;
    private final long charge;

    ExceedCharge(int unit, long charge) {
        this.unit = unit;
        this.charge = charge;
    }

    public Charge calculateOverFare(final long distance) {
        if (distance <= MIN_VALUE) {
            return new Charge(MIN_VALUE);
        }
        return new Charge((int) ((Math.ceil((distance - 1) / unit) + 1) * charge));
    }
}
