package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;

public class Fare {
    private static final int BASIC_PRICE = 1_250;

    private int value;

    public Fare(Lines lines) {
        this.value = BASIC_PRICE + lines.maxExtraFare().get();
    }

    public int get() {
        return value;
    }
}
