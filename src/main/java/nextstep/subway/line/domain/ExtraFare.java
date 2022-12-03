package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {

    public static final int ZERO = 0;

    public static final int BASIC = 1_250;

    public static final int INCREMENT = 100;

    private static final String EXTRA_FARE_THAN_ZERO = "추가요금은 0보다 작을 수 없습니다.";

    @Column(name = "extra_fare", nullable = false)
    private int extraFare;

    protected ExtraFare() {}

    private ExtraFare(int extraFare) {
        if (extraFare < ZERO) {
            throw new IllegalArgumentException(EXTRA_FARE_THAN_ZERO);
        }
        this.extraFare = extraFare;
    }

    public static ExtraFare from(int extraFare) {
        return new ExtraFare(extraFare);
    }

    public ExtraFare add(ExtraFare extraFare) {
        return new ExtraFare(this.extraFare + extraFare.value());
    }

    public ExtraFare subtract(ExtraFare extraFare) {
        return new ExtraFare(this.extraFare - extraFare.value());
    }

    public int value() {
        return extraFare;
    }
}
