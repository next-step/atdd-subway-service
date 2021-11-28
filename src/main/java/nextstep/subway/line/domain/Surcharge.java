package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import javax.persistence.Embeddable;

@Embeddable
public class Surcharge {

    private static final Surcharge ZERO = new Surcharge(0);

    private int value;

    protected Surcharge() {
    }

    private Surcharge(int value) {
        Assert.isTrue(positiveOrEqualZero(value), "추가 요금은 반드시 0 또는 양수이어야 합니다.");
        this.value = value;
    }

    public static Surcharge zero() {
        return ZERO;
    }

    public static Surcharge from(int value) {
        return new Surcharge(value);
    }

    private boolean positiveOrEqualZero(int value) {
        return value > 0;
    }
}
