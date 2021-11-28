package nextstep.subway.common.domain;

import io.jsonwebtoken.lang.Assert;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private static final Fare ZERO = new Fare(0);

    private int value;

    protected Fare() {
    }

    private Fare(int value) {
        Assert.isTrue(positiveOrEqualZero(value), "요금은 반드시 0 또는 양수이어야 합니다.");
        this.value = value;
    }

    public static Fare zero() {
        return ZERO;
    }

    public static Fare from(int value) {
        return new Fare(value);
    }

    public int value() {
        return value;
    }

    private boolean positiveOrEqualZero(int value) {
        return value >= 0;
    }
}
