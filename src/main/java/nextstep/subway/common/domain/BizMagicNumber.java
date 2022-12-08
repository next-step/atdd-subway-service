package nextstep.subway.common.domain;

public enum BizMagicNumber {
    SECTION_MIN_SIZE(1),
    ADDITIONAL_FARE_MIN_BOUNDARY(0);

    private final int number;

    BizMagicNumber(int number) {
        this.number = number;
    }

    public int number() {
        return number;
    }

    @Override
    public String toString() {
        return "BizMagicNumber{" +
                "number=" + number +
                '}';
    }
}
