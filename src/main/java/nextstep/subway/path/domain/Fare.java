package nextstep.subway.path.domain;

public enum Fare {
    BASIC_FARE(1250),
    DISCOUNT_FARE(350);

    private final int value;

    Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
