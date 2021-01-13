package nextstep.subway.fare;

import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    private static final String ERR_TEXT_INVALID_FARE = "요금은 0원 이하일 수 없습니다.";
    private static final int BASE_FARE = 1250;
    private final int fare;

    protected Fare() {
        this.fare = BASE_FARE;
    }

    private Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare of(final int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_FARE);
        }

        return new Fare(fare);
    }

    public static Fare createBaseFare() {
        return new Fare(BASE_FARE);
    }

    public Fare plus(final int otherFare) {
        return new Fare(this.fare + otherFare);
    }

    public Fare minus(final int otherFare) {
        return new Fare(this.fare - otherFare);
    }

    public Fare discountByPercentage(final int percentage) {
        if (percentage == 0) {
            return new Fare(this.fare);
        }

        return this.minus((this.fare / 100) * percentage);
    }

    public int getFare() {
        return fare;
    }
}
