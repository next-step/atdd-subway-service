package nextstep.subway.path.domain;

public enum AgeFarePolicy {
    NOT_ADULT_DEDUCTION(350),

    TEENAGER_MAX_AGE(18),
    TEENAGER_MIN_AGE(13),
    TEENAGER_DISCOUNT_RATE(20),

    KID_MIN_AGE(6),
    KID_MAX_AGE(12),
    KID_DISCOUNT_RATE(50),
    ;

    private int value;

    AgeFarePolicy(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
