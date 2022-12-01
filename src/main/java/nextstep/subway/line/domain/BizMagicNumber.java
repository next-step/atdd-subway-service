package nextstep.subway.line.domain;

public enum BizMagicNumber {
    SECTION_MIN_SIZE(1);

    private int number;

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
