package nextstep.subway.path.domain;

import java.util.function.Function;

public enum OverFare {
    FIVE(5), EIGHT(8);
    private int unit;

    OverFare(int unit) {
        this.unit = unit;
    }

    public int getUnit() {
        return unit;
    }
}
