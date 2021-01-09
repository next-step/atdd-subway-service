package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Fee {
    private static final int DEFAULT_FEE = 1250;

    private int fee;

    protected Fee() {}

    private Fee(int fee) {
        validate(fee);
        this.fee = fee;
    }

    private Fee(int defaultFee, int overFare) {
        validate(overFare);
        this.fee = defaultFee + overFare;
    }

    public static Fee of(int fee) {
        return new Fee(fee);
    }

    public static Fee ofWithOverFare(int overFare) {
        return new Fee(DEFAULT_FEE, overFare);
    }

    public Fee calculateBasicFee(Distance distance) {
        if (isTenToFifty(distance)) {
            return of(fee + calculateFiveOverFare(distance.get()-10));
        }
        if (isMoreThanFifty(distance)) {
            return of(fee + calculateEightOverFare(distance.get()-10));
        }
        return of(DEFAULT_FEE);
    }

    public Fee calculateChildFee() {
        return of((int) ((fee - 350) * 0.5));
    }

    public Fee calculateTeenagerFee() {
        return of((int) ((fee - 350) * 0.8));
    }

    public int get() {
        return fee;
    }

    private void validate(int fee) {
        if (fee < 0) {
            throw new IllegalArgumentException("요금에 대한 금액은 0보다 작을 수 없습니다.");
        }
    }

    private boolean isMoreThanFifty(Distance distance) {
        return distance.get() > 50;
    }

    private boolean isTenToFifty(Distance distance) {
        return 10 < distance.get() && distance.get() <= 50;
    }

    private int calculateFiveOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateEightOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
